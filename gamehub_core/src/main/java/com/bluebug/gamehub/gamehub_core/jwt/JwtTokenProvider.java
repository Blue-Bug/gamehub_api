package com.bluebug.gamehub.gamehub_core.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final CustomUserDetailService customUserDetailService;

    private Key getSigningKey(){
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSECRET_KEY());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Jws<Claims> getClaimsJws(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    public String getUsername(String token){
        return getClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try{
            getClaimsJws(token);
            return true;
        }catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request){
        String header = request.getHeader(jwtProperties.getACCESS_TOKEN_HEADER());
        if(StringUtils.hasText(header) && header.startsWith(GrantType.BEARER)){
            return header.substring(7);
        }
        return null;
    }

    public String resolveToken(String header){
        if(StringUtils.hasText(header) && header.startsWith(GrantType.BEARER)){
            return header.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = customUserDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String generateAccessToken(String username) {
        return generateToken(username,JwtExpirationTime.ACCESS_TOKEN_EXPIRATION_TIME);
    }
    public String generateRefreshToken(String username) {
        return generateToken(username,JwtExpirationTime.REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private String generateToken(String username,Long time){
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + time);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(getSigningKey(),SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getRemainMilliSeconds(String token){
        Date expiration = getClaimsJws(token).getBody().getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

}
