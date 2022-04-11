package com.bluebug.gamehub.token_domain.repository;

import com.bluebug.gamehub.token_domain.domain.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken,String> {
}
