package com.bluebug.gamehub.token_domain.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash("logoutAccessToken")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutAccessToken {
    @Id
    private String id;

    private String username;

    @TimeToLive
    private Long expirationTime;

    public static LogoutAccessToken createLogoutAccessToken(String accessToken, String username, Long remaining_ms){
        return LogoutAccessToken.builder()
                .id(accessToken)
                .username(username)
                .expirationTime(remaining_ms/1000)
                .build();
    }
}
