package com.bluebug.gamehub.gamehub_post_api.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdatePostDto {
    String description;

    Set<String> tags;
}
