package com.bluebug.gamehub.gamehub_auth_api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min=3,max=20)
    private String nickname;

    @Length(min=8,max=20)
    @NotBlank
    private String password;

    @Length(min=8,max=20)
    @NotBlank
    private String passwordConfirm;
}
