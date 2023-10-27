package org.prgrms.nabimarketbe.user.repository;

import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepo extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String name);

//    Optional<User> findByEmail(String email);
//
//    Optional<User> findByEmailAndProvider(String email, String provider);
    Optional<User> findByNicknameAndProvider(String name, String provider);

}
