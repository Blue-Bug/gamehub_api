package com.bluebug.gamehub.gamehub_auth_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProfileDto {
    private String email;

    private String nickname;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
