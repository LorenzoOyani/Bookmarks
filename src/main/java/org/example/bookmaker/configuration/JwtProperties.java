package org.example.bookmaker.configuration;

import org.example.bookmaker.domain.TokenServiceImpl;
import org.example.bookmaker.domain.TokenServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {


    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    @Value("${secret.token.expiration-time}")
    private long jwtExpiration;

    @Bean
    public String getJwtSecret() {
        return JwtPropertiesSingleton.INSTANCE.jwtSecret;
    }
    @Bean
    public long getJwtExpiration() {
        return JwtPropertiesSingleton.INSTANCE.jwtExpiration;
    }

    private static class JwtPropertiesSingleton {
        private static final JwtProperties INSTANCE = new JwtProperties();
    }


    @Bean
    public TokenServices tokenServices(){
        return new TokenServiceImpl();
    }
}
