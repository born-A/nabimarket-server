package org.prgrms.nabimarketbe.domain.user.repository;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepo extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String name);
    Optional<User> findByNicknameAndProvider(String name, String provider);
}
