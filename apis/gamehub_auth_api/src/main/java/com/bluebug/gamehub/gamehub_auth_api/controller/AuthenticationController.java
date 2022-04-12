package com.bluebug.gamehub.gamehub_auth_api.controller;

import com.bluebug.gamehub.gamehub_auth_api.dto.LoginDto;
import com.bluebug.gamehub.gamehub_auth_api.dto.SignUpDto;
import com.bluebug.gamehub.gamehub_auth_api.service.AuthenticationService;
import com.bluebug.gamehub.gamehub_core.dto.ErrorDto;
import com.bluebug.gamehub.gamehub_core.dto.FieldErrorDto;
import com.bluebug.gamehub.gamehub_core.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtProperties jwtProperties;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid SignUpDto signUpDto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(errors.getFieldErrors()
                            .stream()
                            .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                            .collect(Collectors.toList())).build());
        }

        return authenticationService.signUpProcess(signUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDto loginDto, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(errors.getFieldErrors()
                            .stream()
                            .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                            .collect(Collectors.toList())).build());
        }

        return authenticationService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader Map<String,String> header){
        String accessToken = header.get(jwtProperties.getACCESS_TOKEN_HEADER());
        if(accessToken == null || accessToken.isBlank()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto(jwtProperties.getACCESS_TOKEN_HEADER(),"요청에 AccessToken이 없습니다.")))
                    .build());
        }

        return authenticationService.logout(accessToken);
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestHeader Map<String,String> header){
        String accessToken = header.get(jwtProperties.getACCESS_TOKEN_HEADER());
        String refreshToken = header.get(jwtProperties.getREFRESH_TOKEN_HEADER());

        return authenticationService.reissue(accessToken,refreshToken);
    }
}
