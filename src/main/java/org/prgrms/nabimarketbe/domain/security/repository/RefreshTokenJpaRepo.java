package org.prgrms.nabimarketbe.domain.security.repository;

import org.prgrms.nabimarketbe.domain.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByKey(Long key);
}
