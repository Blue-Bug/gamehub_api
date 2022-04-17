package com.bluebug.gamehub.gamehub_s3_api.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Set;

@Getter
@ConstructorBinding
@ConfigurationProperties("media")
@RequiredArgsConstructor
public class FileExtensionUtils {
    private final Set<String> video_extension;
    private final Set<String> image_extension;
    private final String FILE_EXTENSION_SEPARATOR;
    private final String UUID_SEPARATOR;
    private final String CATEGORY_SEPARATOR;
    private final String BUCKET;
}
