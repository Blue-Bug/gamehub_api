package com.bluebug.gamehub.gamehub_auth_api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorDto {
    List<FieldErrorDto> errors;
}
