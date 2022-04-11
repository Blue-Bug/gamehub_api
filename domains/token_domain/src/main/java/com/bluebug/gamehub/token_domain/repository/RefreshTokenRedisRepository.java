package com.bluebug.gamehub.token_domain.repository;

import com.bluebug.gamehub.token_domain.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken,String> {
}
