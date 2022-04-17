package com.bluebug.gamehub.gamehub_s3_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteResultDto {
    private String status;

    private String message;
}
