package com.bluebug.gamehub.gamehub_core.jwt;

import com.bluebug.gamehub.gamehub_core.cache.CacheKey;
import com.bluebug.gamehub.member_domain.domain.Member;
import com.bluebug.gamehub.member_domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Cacheable(value = CacheKey.MEMBER, key = "#username", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> result = memberRepository.findByEmail(username);
        if(result.isEmpty()){
            result = memberRepository.findByNickname(username);
            if(result.isEmpty()) throw new UsernameNotFoundException("해당 정보로 가입한 멤버가 없습니다.");
        }

        return new UserMember(result.get());
    }
}
