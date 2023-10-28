package org.prgrms.nabimarketbe.user.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.prgrms.nabimarketbe.global.exception.CUserNotFoundException;
import org.prgrms.nabimarketbe.sign.dto.UserSignupRequestDto;
import org.prgrms.nabimarketbe.sign.service.SignService;
import org.prgrms.nabimarketbe.user.dto.UserRequestDto;
import org.prgrms.nabimarketbe.user.dto.UserResponseDto;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserJpaRepo userJpaRepo;
    @Autowired
    SignService signService;
    @Autowired
    PasswordEncoder passwordEncoder;

    private UserSignupRequestDto getUserSignupRequestDto(int number) {
        return UserSignupRequestDto.builder()
                .nickname("nickName" + number)
                .build();
    }

    @Test
    public void 회원등록() {
        // given
        UserSignupRequestDto userA =
                getUserSignupRequestDto(1);
        User savedUser = userJpaRepo.save(userA.toEntity());

        // when
        UserResponseDto userB = userService.findById(savedUser.getUserId());
        User byId = userJpaRepo.findById(savedUser.getUserId()).orElseThrow(CUserNotFoundException::new);

        // then
        assertThat(userA.getNickname()).isEqualTo(userB.getNickName());
        assertThat(
                userService.findById(savedUser.getUserId()).getNickName())
                .isEqualTo(byId.getNickname());
    }

    @Test
    public void 회원등록_이메일검증() {
        // given
        UserSignupRequestDto userA =
                getUserSignupRequestDto(1);
        User user = userJpaRepo.save(userA.toEntity());

        // when
        UserResponseDto dtoA = userService.findById(user.getUserId());

        // then
        assertThat(userA.getNickname()).isEqualTo(dtoA.getNickName());
    }

    @Test
    public void 전체_회원조회() {
        // given
        UserSignupRequestDto userA =
                getUserSignupRequestDto(1);
        UserSignupRequestDto userB =
                getUserSignupRequestDto(2);

        // when
        userJpaRepo.save(userA.toEntity());
        userJpaRepo.save(userB.toEntity());

        // then
        List<UserResponseDto> allUser = userService.findAllUser();
        assertThat(allUser.size()).isSameAs(3);
    }

    @Test
    public void 회원수정_닉네임() {
        // given
        UserSignupRequestDto userA =
                getUserSignupRequestDto(1);
        User user = userJpaRepo.save(userA.toEntity());

        // when
        UserRequestDto updateUser = UserRequestDto.builder()
                .nickName("bbb")
                .build();
        userService.update(user.getUserId(), updateUser);

        // then
        assertThat(userService.findById(user.getUserId()).getNickName()).isEqualTo("bbb");
    }

    @Test
    public void 회원삭제() {
        // given
        UserSignupRequestDto userA =
                getUserSignupRequestDto(1);
        User user = userJpaRepo.save(userA.toEntity());

        // when
        userService.delete(user.getUserId());

        // then
        assertThrows(CUserNotFoundException.class, () -> userService.findById(user.getUserId()));
    }

    @Test
    public void BaseTimeEntity_등록() throws Exception {
        //given
        LocalDateTime now = LocalDateTime
                .of(2021, 8, 5, 22, 31, 0);
        UserSignupRequestDto userA =
                getUserSignupRequestDto(1);

        //when
        userJpaRepo.save(userA.toEntity());
        List<User> users = userJpaRepo.findAll();

        //then
        User firstUser = users.get(1);
        assertThat(firstUser.getCreatedDate()).isAfter(now);
        assertThat(firstUser.getModifiedDate()).isAfter(now);
    }
}