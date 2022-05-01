package com.bluebug.gamehub.gamehub_post_api.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@ConstructorBinding
@ConfigurationProperties("cors")
@RequiredArgsConstructor
public class CorsProperties {
    private final List<String> Access_Control_Allow_Origin;
}
