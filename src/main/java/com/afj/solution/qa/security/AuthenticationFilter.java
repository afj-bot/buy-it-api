package com.afj.solution.qa.security;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.afj.solution.qa.service.AppUserDetailsService;

/**
 * @author Tomash Gombosh
 */
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtProvider;

    @Autowired
    private AppUserDetailsService service;

    @Autowired


    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String jwt = jwtProvider.getJwtFromRequest(request);
        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            final String role = jwtProvider.getRoleFromToken(jwt);
            final String username = jwtProvider.getUsernameFromToken(jwt);
            final UserDetails userDetails = service.loadUserByUsername(username);
            if (userDetails.isAccountNonLocked() && userDetails.isEnabled()) {
                final UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(String.format("ROLE_%s", role))));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}