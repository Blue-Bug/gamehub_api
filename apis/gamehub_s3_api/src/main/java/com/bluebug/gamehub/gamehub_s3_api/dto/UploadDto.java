package com.bluebug.gamehub.gamehub_s3_api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UploadDto {
    @Length(max=50)
    String description;

    @Size(max=5)
    Set<@Length(min=1,max=50)String> tags;
}
