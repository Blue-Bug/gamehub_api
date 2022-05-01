package com.bluebug.gamehub.gamehub_post_api.controller;

import com.bluebug.gamehub.gamehub_core.dto.ErrorDto;
import com.bluebug.gamehub.gamehub_core.dto.FieldErrorDto;
import com.bluebug.gamehub.gamehub_post_api.dto.FilterDto;
import com.bluebug.gamehub.gamehub_post_api.dto.UpdatePostDto;
import com.bluebug.gamehub.gamehub_post_api.service.PostService;
import com.bluebug.gamehub.member_domain.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/p/{postId}")
    public ResponseEntity getPost(@PathVariable String postId){
        return postService.getPost(postId);
    }

    @GetMapping("/recent")
    public ResponseEntity getRecent(Pageable pageable){
        return postService.getRecent(pageable);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity getUserFeed(@PathVariable String nickname,Pageable pageable){
        return postService.getUserFeed(nickname,pageable);
    }

    @PatchMapping("/p/{postId}")
    public ResponseEntity updatePost(@CurrentUser Member member, @PathVariable String postId,
                                     @RequestBody UpdatePostDto updatePostDto){
        return postService.updatePost(member,postId,updatePostDto);
    }

    @GetMapping("/search")
    public ResponseEntity getByFilter(@RequestParam("filter") String filter, Pageable pageable,
                                      @RequestAttribute @Valid FilterDto filterDto, Errors errors){
        if(filter.isBlank()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("filterParam"
                            ,"Filter 파라미터가 잘못되었습니다."))));
        }
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(errors.getFieldErrors()
                            .stream()
                            .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                            .collect(Collectors.toList())).build());
        }
        return postService.getByFilter(filter,filterDto,pageable);
    }
}
