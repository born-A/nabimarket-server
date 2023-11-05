package org.prgrms.nabimarketbe.user.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserSignInRequestDTO;
import org.prgrms.nabimarketbe.domain.user.service.SignService;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SignService signService;

    private UserSignInRequestDTO getUserSignupRequestDto(int number) {
        return UserSignInRequestDTO.builder()
                .nickname("nickName" + number)
                .build();
    }

    @Test
    public void 회원등록() {
        // given
        UserSignInRequestDTO userA =
                getUserSignupRequestDto(1);
        User savedUser = userRepository.save(userA.toEntity());

        // when
        UserResponseDTO userB = userService.findById(savedUser.getUserId());
        User byId = userRepository.findById(savedUser.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        // then
        assertThat(userA.nickname()).isEqualTo(userB.nickName());
        assertThat(
                userService.findById(savedUser.getUserId()).nickName())
                .isEqualTo(byId.getNickname());
    }

    @Test
    public void 회원등록_이메일검증() {
        // given
        UserSignInRequestDTO userA =
                getUserSignupRequestDto(1);
        User user = userRepository.save(userA.toEntity());

        // when
        UserResponseDTO dtoA = userService.findById(user.getUserId());

        // then
        assertThat(userA.nickname()).isEqualTo(dtoA.nickName());
    }

    @Test
    public void 전체_회원조회() {
        // given
        UserSignInRequestDTO userA =
                getUserSignupRequestDto(1);
        UserSignInRequestDTO userB =
                getUserSignupRequestDto(2);

        // when
        userRepository.save(userA.toEntity());
        userRepository.save(userB.toEntity());

        // then
        List<UserResponseDTO> allUser = userService.findAllUser();
        assertThat(allUser.size()).isSameAs(3);
    }

    @Test
    public void 회원수정_닉네임() {
        // given
        UserSignInRequestDTO userA =
                getUserSignupRequestDto(1);
        User user = userRepository.save(userA.toEntity());

        // when
        UserRequestDTO updateUser = UserRequestDTO.builder()
                .nickName("bbb")
                .build();

        userService.updateUserNickname(user.getUserId(), updateUser);

        // then
        assertThat(userService.findById(user.getUserId()).nickName()).isEqualTo("bbb");
    }

    @Test
    public void 회원삭제() {
        // given
        UserSignInRequestDTO userA =
                getUserSignupRequestDto(1);
        User user = userRepository.save(userA.toEntity());

        // when
        userService.deleteUser(user.getUserId());

        // then
        assertThrows(RuntimeException.class, () -> userService.findById(user.getUserId()));
    }

    @Test
    public void BaseTimeEntity_등록() throws Exception {
        //given
        LocalDateTime now = LocalDateTime
                .of(2021, 8, 5, 22, 31, 0);
        UserSignInRequestDTO userA =
                getUserSignupRequestDto(1);

        //when
        userRepository.save(userA.toEntity());
        List<User> users = userRepository.findAll();

        //then
        User firstUser = users.get(1);
        assertThat(firstUser.getCreatedDate()).isAfter(now);
        assertThat(firstUser.getModifiedDate()).isAfter(now);
    }
}