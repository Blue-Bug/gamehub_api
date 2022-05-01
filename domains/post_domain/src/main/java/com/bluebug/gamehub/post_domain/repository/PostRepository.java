package com.bluebug.gamehub.post_domain.repository;

import com.bluebug.gamehub.post_domain.domain.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends MongoRepository<Post,String> {
    
    List<Post> findByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByNicknameOrderByCreatedAtDesc(String nickname, Pageable pageable);

    List<Post> findByTagsContaining(Set<String> tags, Pageable pageable);
}
