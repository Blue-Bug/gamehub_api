package com.bluebug.gamehub.member_domain.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@EqualsAndHashCode(of = "id")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id
    private String id;

    private String email;

    private String nickname;

    private String password;

    @Builder.Default
    private Set<String> roles = new HashSet<>();

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public static Member createUser(String email, String nickname, String password){
        Member newMember = buildMember(email,nickname, password);
        newMember.roles.add("ROLE_USER");
        return newMember;
    }

    public static Member createAdmin(String email, String nickname, String password){
        Member newMember = buildMember(email, nickname, password);
        newMember.roles.add("ROLE_USER");
        return newMember;
    }

    private static Member buildMember(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }
}
