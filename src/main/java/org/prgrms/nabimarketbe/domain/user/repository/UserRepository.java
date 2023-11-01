package org.prgrms.nabimarketbe.domain.user.repository;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByAccountIdAndProvider(String accountId, String provider);

    Optional<User> findByAccountId(String oauthId);
}
