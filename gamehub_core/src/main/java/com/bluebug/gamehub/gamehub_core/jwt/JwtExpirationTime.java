package com.bluebug.gamehub.gamehub_core.jwt;

public interface JwtExpirationTime {
    Long ACCESS_TOKEN_EXPIRATION_TIME  = 1800000L;;
    Long REFRESH_TOKEN_EXPIRATION_TIME = 1209600000L;
    Long REISSUE_TOKEN_EXPIRATION_TIME = 259200000L;
}
