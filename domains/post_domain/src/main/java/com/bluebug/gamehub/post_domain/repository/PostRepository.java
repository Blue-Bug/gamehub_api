package com.bluebug.gamehub.post_domain.repository;

import com.bluebug.gamehub.post_domain.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {
}
