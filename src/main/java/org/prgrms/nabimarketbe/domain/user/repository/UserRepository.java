package org.prgrms.nabimarketbe.domain.user.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String name);

    Optional<User> findByEmail(String email);

    Optional<User> findByNicknameAndProvider(String name, String provider);

    Optional<User> findByNameAttributeKey(String nameAttributeKey);
}
