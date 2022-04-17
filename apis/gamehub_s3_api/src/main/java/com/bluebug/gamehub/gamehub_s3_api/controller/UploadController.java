package com.bluebug.gamehub.gamehub_s3_api.controller;

import com.bluebug.gamehub.gamehub_core.dto.ErrorDto;
import com.bluebug.gamehub.gamehub_core.dto.FieldErrorDto;
import com.bluebug.gamehub.gamehub_core.jwt.JwtProperties;
import com.bluebug.gamehub.gamehub_s3_api.dto.UploadDto;
import com.bluebug.gamehub.gamehub_s3_api.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/p")
public class UploadController {
    private final JwtProperties jwtProperties;
    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity uploadPost(@CurrentUser String nickname,
                                     @RequestPart("dto") @Valid UploadDto uploadDto, Errors errors,
                                     @RequestPart("file") List<MultipartFile> media){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(errors.getFieldErrors()
                            .stream()
                            .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                            .collect(Collectors.toList())).build());
        }

        if(nickname == null || nickname.isBlank()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto(jwtProperties.getACCESS_TOKEN_HEADER()
                            ,"요청에 AccessToken이 없습니다.")))
                    .build());
        }

        return uploadService.uploadPost(nickname,uploadDto,media);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@CurrentUser String nickname,@PathVariable String postId){
        if(nickname == null || nickname.isBlank()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                            .errors(List.of(new FieldErrorDto(jwtProperties.getACCESS_TOKEN_HEADER()
                                    ,"요청에 AccessToken이 없습니다.")))
                            .build());
        }
        if(postId == null || postId.isBlank()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                            .errors(List.of(new FieldErrorDto("Post ID"
                                    ,"요청에 Post ID가 없습니다.")))
                            .build());
        }

        return uploadService.deletePost(nickname,postId);
    }

}
