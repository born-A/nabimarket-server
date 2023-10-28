package org.prgrms.nabimarketbe.domain.security.oauth.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.user.repository.UserJpaRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJpaRepo userJpaRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userJpaRepo.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));
    }
}
