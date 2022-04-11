package com.bluebug.gamehub.gamehub_auth_api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank
    private String username;

    @Length(min=8,max=20)
    @NotBlank
    private String password;
}
