package com.afj.solution.buyitapp.service.converters.user;

import java.util.HashSet;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.afj.solution.buyitapp.model.User;
import com.afj.solution.buyitapp.payload.request.CreateUserRequest;
import com.afj.solution.buyitapp.service.converters.Converter;

/**
 * @author Kristian Gombosh
 */
@Slf4j
@Service
public class CreateUserRequestToUser implements Converter<CreateUserRequest, User> {

    @Override
    public User convert(final CreateUserRequest createUserRequest) {
        log.info("Convert the Create User Request ({}) to user", createUserRequest);
        return new User(user -> {
            user.setFirstName(createUserRequest.getFirstName());
            user.setLastName(createUserRequest.getLastName());
            user.setUsername(createUserRequest.getUsername());
            user.setEmail(createUserRequest.getEmail());
            user.setDateOfBirth(createUserRequest.getDateOfBirth());
            user.setHomeAddress(createUserRequest.getHomeAddress());
            user.setPassword(createUserRequest.getPassword());
            user.setPrivacyPolicy(createUserRequest.isPrivacyPolicy());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setEnabled(true);
            user.setCredentialsNonExpired(true);
            user.setAuthorities(new HashSet<>(List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
        });
    }
}
