package com.bluebug.gamehub.gamehub_post_api.service;

import com.bluebug.gamehub.gamehub_core.dto.ErrorDto;
import com.bluebug.gamehub.gamehub_core.dto.FieldErrorDto;
import com.bluebug.gamehub.gamehub_post_api.dto.FeedDto;
import com.bluebug.gamehub.gamehub_post_api.dto.PostDto;
import com.bluebug.gamehub.gamehub_post_api.dto.FilterDto;
import com.bluebug.gamehub.gamehub_post_api.dto.UpdatePostDto;
import com.bluebug.gamehub.member_domain.domain.Member;
import com.bluebug.gamehub.post_domain.domain.Post;
import com.bluebug.gamehub.post_domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public ResponseEntity getRecent(Pageable pageable) {
        List<Post> pagedPost = postRepository.findByOrderByCreatedAtDesc(pageable);

        List<PostDto> postDtoList = new ArrayList<>();
        for(Post post : pagedPost){
            postDtoList.add(PostDto.builder()
                    .nickname(post.getNickname())
                    .tags(post.getTags())
                    .description(post.getDescription())
                    .filePaths(post.getFilePaths())
                    .createdAt(post.getCreatedAt())
                    .build());
        }

        return ResponseEntity.ok().body(postDtoList);
    }

    public ResponseEntity getPost(String postId) {
        Optional<Post> byId = postRepository.findById(postId);
        if(byId.isEmpty()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("post_id","post가 존재하지 않습니다.")))
                    .build());
        }

        PostDto postDto = PostDto.builder()
                .nickname(byId.get().getNickname())
                .tags(byId.get().getTags())
                .description(byId.get().getDescription())
                .filePaths(byId.get().getFilePaths())
                .createdAt(byId.get().getCreatedAt())
                .build();
        return ResponseEntity.ok().body(postDto);
    }

    public ResponseEntity getUserFeed(String nickname,Pageable pageable) {
        List<Post> byNickname = postRepository.findByNicknameOrderByCreatedAtDesc(nickname, pageable);

        List<FeedDto> feedDtos = new ArrayList<>();
        for(Post post : byNickname){
            feedDtos.add(FeedDto.builder()
                    .tags(post.getTags())
                    .createdAt(post.getCreatedAt())
                    .postUrl(post.getId())
                    .thumbnail(post.getThumbnail()).build());
        }

        return ResponseEntity.ok().body(feedDtos);
    }

    @Transactional
    public ResponseEntity updatePost(Member member, String postId, UpdatePostDto updatePostDto) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("postId","post가 존재하지 않습니다.")))
                    .build());
        }
        if(!post.getNickname().equals(member.getNickname())){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("member","해당 Post를 작성한 Member가 아닙니다."))));
        }
        post.setTags(updatePostDto.getTags());
        post.setDescription(updatePostDto.getDescription());
        postRepository.save(post);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity getByFilter(String filter,FilterDto filterDto, Pageable pageable) {
        if(filter.equals("tags")){
            List<Post> byTagsContaining = postRepository.findByTagsContaining(filterDto.getTags(), pageable);

            List<PostDto> postDtos = new ArrayList<>();
            for(Post post : byTagsContaining){
                postDtos.add(PostDto.builder()
                                .nickname(post.getNickname())
                                .description(post.getDescription())
                                .tags(post.getTags())
                                .filePaths(post.getFilePaths())
                                .createdAt(post.getCreatedAt())
                                .build());
            }

            return ResponseEntity.ok().body(postDtos);
        }

        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .errors(List.of(new FieldErrorDto("filterParam"
                        ,"Filter 파라미터가 잘못되었습니다."))));
    }
}
