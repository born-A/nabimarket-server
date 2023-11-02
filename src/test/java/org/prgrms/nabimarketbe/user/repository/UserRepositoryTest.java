package org.prgrms.nabimarketbe.user.repository;

import org.junit.jupiter.api.Test;
import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private String nickname = "xinxinzara";
    private String password = "myPassWord";

    @Test
    public void 회원저장_후_이메일로_회원검색() throws Exception {

        //given
        userRepository.save(User.builder()
                .nickname(nickname)
                .role(Role.USER)
                .build());

        //when
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        //then
        assertNotNull(user);
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @Test
    public void 카카오_가입자_조회() throws Exception
    {
        //given
        userRepository.save(User.builder()
                .nickname(nickname)
                .provider("kakao")
                .build());
        //when
        Optional<User> kakao = userRepository.findByNicknameAndProvider(nickname, "kakao");

        //then
        assertThat(kakao).isNotNull();
        assertThat(kakao.isPresent()).isEqualTo(true);
        assertThat(kakao.get().getNickname()).isEqualTo(nickname);
        assertThat(kakao.get().getProvider()).isEqualTo("kakao");
    }
}