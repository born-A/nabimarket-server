package org.prgrms.nabimarketbe.domain.user.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String name);

    Optional<User> findByAccountIdAndProvider(String accountId, String provider);

    Optional<User> findByAccountId(String oauthId);

    boolean existsUserByNickname(String nickname);
}
