package org.example.bookmaker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
@AllArgsConstructor
public class UserInfoProperties {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
}
