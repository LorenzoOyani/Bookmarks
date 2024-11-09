package org.example.bookmaker.security;

import lombok.RequiredArgsConstructor;
import org.example.bookmaker.domain.UserInfoProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthDetailService implements UserDetailsService {

    private UserInfoProperties userDetailsInfo;

    public AuthDetailService(UserInfoProperties userDetailsInfo) {
        this.userDetailsInfo = userDetailsInfo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (ObjectUtils.isEmpty(username) || !username.equals(userDetailsInfo.getUsername())) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
        return new User(
                userDetailsInfo.getUsername(),
                userDetailsInfo.getPassword(),
                new ArrayList<>());
    }
}
