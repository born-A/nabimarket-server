package org.prgrms.nabimarketbe.oauth2.kakao.repository;

import org.prgrms.nabimarketbe.global.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByKey(Long key);
}
