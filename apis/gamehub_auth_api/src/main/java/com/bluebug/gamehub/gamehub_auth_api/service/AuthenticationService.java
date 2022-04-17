package com.bluebug.gamehub.gamehub_auth_api.service;

import com.bluebug.gamehub.gamehub_auth_api.dto.*;
import com.bluebug.gamehub.gamehub_core.dto.ErrorDto;
import com.bluebug.gamehub.gamehub_core.dto.FieldErrorDto;
import com.bluebug.gamehub.gamehub_core.jwt.GrantType;
import com.bluebug.gamehub.gamehub_core.jwt.JwtExpirationTime;
import com.bluebug.gamehub.gamehub_core.jwt.JwtProperties;
import com.bluebug.gamehub.gamehub_core.jwt.JwtTokenProvider;
import com.bluebug.gamehub.member_domain.domain.Member;
import com.bluebug.gamehub.member_domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.bluebug.gamehub.token_domain.domain.LogoutAccessToken;
import com.bluebug.gamehub.token_domain.domain.RefreshToken;
import com.bluebug.gamehub.token_domain.repository.LogoutAccessTokenRedisRepository;
import com.bluebug.gamehub.token_domain.repository.RefreshTokenRedisRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    public ResponseEntity signUpProcess(SignUpDto signUpDto){
        if(memberRepository.existsByEmail(signUpDto.getEmail())){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("email","이미 가입한 이메일입니다.")))
                    .build());
        }
        if(memberRepository.existsByNickname(signUpDto.getNickname())){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("nickname","이미 가입한 닉네임입니다.")))
                    .build());
        }
        if(!signUpDto.getPassword().equals(signUpDto.getPasswordConfirm())){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("password","비밀번호가 일치하지 않습니다.")))
                    .build());
        }

        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        signUpDto.setPasswordConfirm(passwordEncoder.encode(signUpDto.getPasswordConfirm()));

        Member newMember = memberRepository.save(Member.createUser(signUpDto.getEmail(),
                signUpDto.getNickname(), signUpDto.getPassword()));


        return ResponseEntity.ok().location(URI.create(newMember.getNickname()))
                .body(ProfileDto.builder()
                        .email(newMember.getEmail())
                        .nickname(newMember.getNickname())
                        .created_at(newMember.getCreated_at())
                        .updated_at(newMember.getUpdated_at()).build());
    }

    public ResponseEntity login(LoginDto loginDto, HttpServletResponse response) {
        Optional<Member> result = memberRepository.findByEmail(loginDto.getUsername());
        if(result.isEmpty()){
            result = memberRepository.findByNickname(loginDto.getUsername());
            if(result.isEmpty()){
                return ResponseEntity.badRequest().body(ErrorDto.builder()
                        .errors(List.of(new FieldErrorDto("username","이메일/닉네임이 잘못 되었습니다.")))
                        .build());
            }
        }

        if(!passwordEncoder.matches(loginDto.getPassword(),result.get().getPassword())){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("password","비밀번호가 일치하지 않습니다.")))
                    .build());
        }

        String username = result.get().getEmail();
        String accessToken = jwtTokenProvider.generateAccessToken(username);
        RefreshToken refreshToken = saveAndReturnRefreshToken(username);

        addRefreshTokenToCookie(response, refreshToken);

        return ResponseEntity.ok().body(TokenDto.builder()
                .grantType(GrantType.BEARER)
                .accessToken(accessToken)
                .build());
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, RefreshToken refreshToken) {
        Cookie cookie = new Cookie(jwtProperties.getREFRESH_TOKEN_HEADER(), refreshToken.getRefreshToken());

        cookie.setMaxAge(refreshToken.getExpirationTime().intValue());

        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private RefreshToken saveAndReturnRefreshToken(String username) {
        return refreshTokenRedisRepository.save(
                RefreshToken.createRefreshToken(
                        username,
                        jwtTokenProvider.generateRefreshToken(username),
                        JwtExpirationTime.REFRESH_TOKEN_EXPIRATION_TIME));
    }

    public ResponseEntity logout(String accessToken) {
        accessToken = jwtTokenProvider.resolveToken(accessToken);

        if(!jwtTokenProvider.validateToken(accessToken)){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto(jwtProperties.getACCESS_TOKEN_HEADER(), "AccessToken에 문제가 있습니다.")))
                    .build());
        }

        String username = jwtTokenProvider.getUsername(accessToken);

        refreshTokenRedisRepository.deleteById(username);
        Long remainMilliSeconds = jwtTokenProvider.getRemainMilliSeconds(accessToken);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken
                .createLogoutAccessToken(accessToken, username, remainMilliSeconds));

        return ResponseEntity.ok().body(ResultDto.builder()
                .status("logout_result")
                .message("정상적으로 로그아웃 되었습니다.")
                .build());
    }

    public ResponseEntity reissue(String accessToken, String refreshToken, HttpServletResponse response) {
        accessToken = jwtTokenProvider.resolveToken(accessToken);

        boolean isAccessTokenValid = jwtTokenProvider.validateToken(accessToken);
        boolean isRefreshTokenValid = false;

        if(jwtTokenProvider.validateToken(refreshToken)){
            isRefreshTokenValid = refreshTokenRedisRepository.existsById(jwtTokenProvider.getUsername(refreshToken));
        }

        if(isAccessTokenValid && isRefreshTokenValid){
            return ResponseEntity.ok().body(TokenDto.builder()
                    .grantType(GrantType.BEARER)
                    .accessToken(accessToken)
                    .build());
        }
        else if(isAccessTokenValid){
            addRefreshTokenToCookie(response,saveAndReturnRefreshToken(jwtTokenProvider.getUsername(accessToken)));
            return ResponseEntity.ok().body(TokenDto.builder()
                    .grantType(GrantType.BEARER)
                    .accessToken(accessToken)
                    .build());
        }
        else if(isRefreshTokenValid){
            String username = jwtTokenProvider.getUsername(refreshToken);

            TokenDto tokenDto = TokenDto.builder()
                    .grantType(GrantType.BEARER)
                    .accessToken(jwtTokenProvider.generateAccessToken(username))
                    .build();

            if(jwtTokenProvider.getRemainMilliSeconds(refreshToken) < JwtExpirationTime.REISSUE_TOKEN_EXPIRATION_TIME){
                refreshTokenRedisRepository.deleteById(username);
                addRefreshTokenToCookie(response,saveAndReturnRefreshToken(username));
            }

            return ResponseEntity.ok().body(tokenDto);
        }

        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .errors(List.of(
                        new FieldErrorDto(jwtProperties.getACCESS_TOKEN_HEADER()
                                ,"Token이 전부 만료되었습니다.")))
                .build());
    }
}
