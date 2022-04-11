package com.bluebug.gamehub.member_domain.repository;

import com.bluebug.gamehub.member_domain.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member,String> {
    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
