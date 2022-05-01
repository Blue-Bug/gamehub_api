package com.bluebug.gamehub.gamehub_post_api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class PostDto {
    private String nickname;

    private String description;

    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @Builder.Default
    private List<String> filePaths = new ArrayList<>();

    private LocalDateTime createdAt;
}
