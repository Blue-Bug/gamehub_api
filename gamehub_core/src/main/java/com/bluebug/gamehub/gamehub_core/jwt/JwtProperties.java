package com.bluebug.gamehub.gamehub_core.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("jwt")
@RequiredArgsConstructor
public class JwtProperties {
    private final String SECRET_KEY;
    private final String ACCESS_TOKEN_HEADER;
    private final String REFRESH_TOKEN_HEADER;
}
