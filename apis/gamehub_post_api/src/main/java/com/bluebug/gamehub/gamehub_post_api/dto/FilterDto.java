package com.bluebug.gamehub.gamehub_post_api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class FilterDto {
    @Size(max=5)
    Set<@Length(min=1,max=50)String> tags;
}
