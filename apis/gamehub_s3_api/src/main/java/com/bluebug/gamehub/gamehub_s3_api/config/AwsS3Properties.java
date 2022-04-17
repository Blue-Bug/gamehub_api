package com.bluebug.gamehub.gamehub_s3_api.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("cloud.aws.credentials")
@RequiredArgsConstructor
public class AwsS3Properties {
    private final String accessKey;

    private final String secretKey;

    private final String region;

    private final String endpoint;
}
