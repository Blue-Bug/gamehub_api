package com.bluebug.gamehub.gamehub_auth_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
