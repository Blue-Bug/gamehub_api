package com.bluebug.gamehub.gamehub_s3_api.dto;

import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
@Builder
public class UploadResultDto {
    private String status;

    private URI createdUrl;

    List<String> filePaths;
}
