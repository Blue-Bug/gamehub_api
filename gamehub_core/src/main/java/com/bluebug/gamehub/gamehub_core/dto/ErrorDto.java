package com.bluebug.gamehub.gamehub_core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorDto {
    List<FieldErrorDto> errors;
}
