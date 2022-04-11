package com.bluebug.gamehub.gamehub_auth_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDto {
    private String status;
    private String message;
}
