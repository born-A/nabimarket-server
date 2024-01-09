package org.prgrms.nabimarketbe.jpa.refreshtoken.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.jpa.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
}
