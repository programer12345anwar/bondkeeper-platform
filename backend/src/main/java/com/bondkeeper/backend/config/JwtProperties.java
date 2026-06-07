package com.bondkeeper.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bondkeeper.jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs;
    private String issuer;
}
