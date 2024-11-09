package org.example.bookmaker.domain;

public interface TokenServices {

    TokenResponse generateToken(TokenRequest request);
}
