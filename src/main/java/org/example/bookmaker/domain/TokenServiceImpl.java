package org.example.bookmaker.domain;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.bookmaker.security.AuthDetailService;
import org.example.bookmaker.security.JwtHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@AllArgsConstructor
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenServices {

    private AuthenticationManager authenticationManager;

    private AuthDetailService userDetailsService;

    private JwtHelper jwtHelper;

    @Override
    public TokenResponse generateToken(TokenRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtHelper.createToken(Collections.EMPTY_MAP, userDetails.getUsername());


        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
