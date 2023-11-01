package org.prgrms.nabimarketbe.oauth2.kakao.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.global.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserId(Long userId);
}
