package com.afj.solution.buyitapp.service.user;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.Cookie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.afj.solution.buyitapp.constans.Redirects;
import com.afj.solution.buyitapp.exception.CustomAuthenticationException;
import com.afj.solution.buyitapp.model.TemporaryToken;
import com.afj.solution.buyitapp.model.User;
import com.afj.solution.buyitapp.payload.request.LoginRequest;
import com.afj.solution.buyitapp.repository.UserRepository;
import com.afj.solution.buyitapp.security.JwtTokenProvider;
import com.afj.solution.buyitapp.service.TemporaryTokenServiceImpl;
import com.afj.solution.buyitapp.service.cookie.AnonymousCookieService;
import com.afj.solution.buyitapp.service.cookie.CookieService;
import com.afj.solution.buyitapp.service.localize.TranslatorService;

import static java.util.Objects.isNull;

/**
 * @author Tomash Gombosh
 */
@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final Duration tokenExpiration;
    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final CookieService anonymousCookieService;

    private final TemporaryTokenServiceImpl temporaryTokenService;

    private final UserLoginService userLoginService;

    private final TranslatorService translator;
    private final Redirects redirects;

    @Autowired
    public UserAuthServiceImpl(final UserRepository userRepository,
                               final JwtTokenProvider jwtTokenProvider,
                               final AuthenticationManager authenticationManager,
                               final AnonymousCookieService anonymousCookieService,
                               final TemporaryTokenServiceImpl temporaryTokenService,
                               final UserLoginService userLoginService,
                               final TranslatorService translator,
                               final Redirects redirects,
                               @Value("${token.expiration.anonymous}") final long tokenExpiration) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.anonymousCookieService = anonymousCookieService;
        this.temporaryTokenService = temporaryTokenService;
        this.userLoginService = userLoginService;
        this.translator = translator;
        this.redirects = redirects;
        this.tokenExpiration = Duration.ofHours(tokenExpiration);
    }

    @Override
    public User findByUsername(final String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new CustomAuthenticationException(translator
                                .toLocale("error.username-password.invalid")));
    }

    @Override
    public User findById(final UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new CustomAuthenticationException(translator
                                .toLocale("error.username-password.invalid")));
    }

    @Override
    public Object login(final LoginRequest loginRequest) {
        final String username = loginRequest.getUsername();
        final User user = this
                .findByUsername(username);
        if (!user.isPrivacyPolicy()) {
            log.info("User {} not have accepted privacy policy", user);
            log.info("Redirect to the {} the user {}", redirects.getUserPrivacyPolicyUrl(), user.getId());
            return ResponseEntity
                    .status(302)
                    .header("Location", redirects.getUserPrivacyPolicyUrl())
                    .build();
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    loginRequest.getPassword(), user.getAuthorities()));
            userLoginService.updateLoginAttempts(user);
        } catch (BadCredentialsException ex) {
            userLoginService.checkLoginAttempts(user);
        }
        return jwtTokenProvider.createToken(user);
    }

    @Override
    public String loginAnonymous(final String anonymousCookie, final UUID userId) {
        final Set<GrantedAuthority> roles = new HashSet<>(List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        final AnonymousAuthenticationToken authenticationToken = new AnonymousAuthenticationToken("anonymous", userId.toString(), roles);
        authenticationManager.authenticate(authenticationToken);

        final Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("id", userId);
        claims.put("roles", roles);
        claims.put("username", "Anonymous");
        return jwtTokenProvider.createToken(claims, tokenExpiration);
    }

    @Override
    public Cookie checkAnonymousCookie(final Cookie[] cookies) {
        if (isNull(cookies) || cookies.length == 0) {
            throw new CustomAuthenticationException("error.cookie.invalid");
        }
        final Cookie cookie = Arrays.stream(cookies)
                .filter(c -> "anonymous".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new CustomAuthenticationException("error.cookie.invalid"));
        final UUID decodeToken = UUID.fromString(anonymousCookieService.decodeCookie(cookie));
        if (!temporaryTokenService.isTokenExist(decodeToken)) {
            throw new CustomAuthenticationException("error.cookie.invalid");
        }
        return cookie;
    }

    @Override
    public ResponseCookie generateAnonymousCookie() {
        final TemporaryToken temporaryToken = temporaryTokenService.save();
        return anonymousCookieService.generateCookie("anonymous", temporaryToken.getId().toString());
    }
}
