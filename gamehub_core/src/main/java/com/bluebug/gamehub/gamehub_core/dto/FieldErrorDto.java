package com.bluebug.gamehub.gamehub_core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FieldErrorDto {
    private String field;

    private String errorMessage;
}
