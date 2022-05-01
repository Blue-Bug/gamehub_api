package com.bluebug.gamehub.gamehub_post_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class FeedDto {
    String thumbnail;

    String postUrl;

    Set<String> tags;

    LocalDateTime createdAt;
}
