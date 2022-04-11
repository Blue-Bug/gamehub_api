package com.bluebug.gamehub.gamehub_core.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import com.bluebug.gamehub.token_domain.repository.LogoutAccessTokenRedisRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        try{
            if(token != null && jwtTokenProvider.validateToken(token)){
                if(logoutAccessTokenRedisRepository.existsById(token)){
                    request.setAttribute("Unauthorized","로그아웃 된 상태입니다.");
                }
                else{
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            else{
                request.setAttribute("Unauthorized","401 Error 요청에 인증키가 없습니다.");
            }
        }
        catch (UsernameNotFoundException e){
            request.setAttribute("Unauthorized", "해당 인증키로 가입한 멤버가 없습니다.");
        }
        catch (ExpiredJwtException e){
            request.setAttribute("Unauthorized", "401 Error 인증키가 만료되었습니다.");
        }
        finally {
            filterChain.doFilter(request,response);
        }
    }
}
