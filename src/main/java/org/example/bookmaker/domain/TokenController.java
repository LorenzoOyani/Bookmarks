package org.example.bookmaker.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenServices tokenService;

    @PostMapping("/token/create")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest) {

        TokenResponse response = tokenService.generateToken(tokenRequest);
        return ResponseEntity.ok(response);

    }
}
