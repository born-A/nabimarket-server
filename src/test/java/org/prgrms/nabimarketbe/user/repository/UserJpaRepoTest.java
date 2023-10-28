package org.prgrms.nabimarketbe.user.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.prgrms.nabimarketbe.global.exception.CUserNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserJpaRepoTest {

    @Autowired
    private UserJpaRepo userJpaRepo;

    private String nickname = "xinxinzara";
    private String password = "myPassWord";

    @Test
    public void 회원저장_후_이메일로_회원검색() throws Exception {

        //given
        userJpaRepo.save(User.builder()
                .nickname(nickname)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        //when
        User user = userJpaRepo.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        //then
        assertNotNull(user);
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @Test
    public void 카카오_가입자_조회() throws Exception
    {
        //given
        userJpaRepo.save(User.builder()
                .nickname(nickname)
                .provider("kakao")
                .build());
        //when
        Optional<User> kakao = userJpaRepo.findByNicknameAndProvider(nickname, "kakao");

        //then
        assertThat(kakao).isNotNull();
        assertThat(kakao.isPresent()).isEqualTo(true);
        assertThat(kakao.get().getNickname()).isEqualTo(nickname);
        assertThat(kakao.get().getProvider()).isEqualTo("kakao");
    }
}