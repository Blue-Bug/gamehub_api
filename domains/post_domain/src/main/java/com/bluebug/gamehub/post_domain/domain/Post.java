package com.bluebug.gamehub.post_domain.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document
@EqualsAndHashCode(of = "id")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    private String id;

    private String nickname;

    private String description;

    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @Builder.Default
    private List<String> filePaths = new ArrayList<>();

    private String thumbnail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Post createPost(String nickname,String description, String thumbnail,
                                  Set<String> tags,List<String> pathList){
        return Post.builder()
                .nickname(nickname)
                .description(description)
                .thumbnail(thumbnail)
                .tags(tags)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .filePaths(pathList)
                .build();
    }
}
