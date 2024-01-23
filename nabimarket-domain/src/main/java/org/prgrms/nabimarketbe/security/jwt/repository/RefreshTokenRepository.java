package org.prgrms.nabimarketbe.security.jwt.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
}
