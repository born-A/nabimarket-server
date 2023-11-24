package org.prgrms.nabimarketbe.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserNicknameUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserProfileUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserGetResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.prgrms.nabimarketbe.setup.user.UserBuilder;
import org.prgrms.nabimarketbe.setup.user.request.UserNicknameUpdateRequestDTOBuilder;
import org.prgrms.nabimarketbe.setup.user.request.UserProfileUpdateRequestDTOBuilder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    S3FileUploadService s3FileUploadService;

    @Mock
    CheckService checkService;

    static final String TEST_TOKEN = "testToken";

    @DisplayName("유저를 토큰으로 조회할 수 있다.")
    @Test
    void GetUserByTokenTest() {
        // given
        User user = UserBuilder.createTestEntity();
        UserResponseDTO<UserGetResponseDTO> expectedResponseDTO = new UserResponseDTO<>(UserGetResponseDTO.from(user));

        given(checkService.parseToken(TEST_TOKEN)).willReturn(1L);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));

        // when
        UserResponseDTO<UserGetResponseDTO> actualResponseDTO = userService.getUserByToken(TEST_TOKEN);

        // then
        assertThat(actualResponseDTO.userInfo()).usingRecursiveComparison()
            .ignoringFields("userId", "imageUrl")
            .isEqualTo(expectedResponseDTO.userInfo());
    }

    @DisplayName("유저 이미지를 업데이트 할 수 있다.")
    @Test
    void UpdateUserImageTest() {
        // given
        UserProfileUpdateRequestDTO requestDTO = UserProfileUpdateRequestDTOBuilder.createTestDTO();
        User user = UserBuilder.createTestEntity();

        given(checkService.parseToken(TEST_TOKEN)).willReturn(1L);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        willDoNothing().given(s3FileUploadService).deleteImage(anyString());

        // when
        userService.updateUserImageUrl(TEST_TOKEN, requestDTO);

        // then
        assertThat(user.getImageUrl()).isEqualTo(requestDTO.imageUrl());
    }

    @DisplayName("유저 닉네임을 업데이트 할 수 있다.")
    @Test
    void UpdateUserNicknameTest() {
        // given
        UserNicknameUpdateRequestDTO requestDTO = UserNicknameUpdateRequestDTOBuilder.createTestDTO();
        User user = UserBuilder.createTestEntity();

        given(checkService.parseToken(TEST_TOKEN)).willReturn(1L);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));

        // when
        userService.updateUserNickname(TEST_TOKEN, requestDTO);

        // then
        assertThat(user.getNickname()).isEqualTo(requestDTO.nickname());
    }
}
