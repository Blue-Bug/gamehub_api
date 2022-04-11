package com.bluebug.gamehub.token_domain.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash("refreshToken")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    private String id;

    private String refreshToken;

    @TimeToLive
    private Long expirationTime;

    public static RefreshToken createRefreshToken(String username, String refreshToken, Long remaining_ms){
        return RefreshToken.builder()
                .id(username)
                .refreshToken(refreshToken)
                .expirationTime(remaining_ms/1000)
                .build();
    }
}
