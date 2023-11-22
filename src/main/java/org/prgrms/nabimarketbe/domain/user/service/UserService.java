package org.prgrms.nabimarketbe.domain.user.service;

import javax.validation.Valid;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserNicknameUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserProfileUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserGetResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final S3FileUploadService s3FileUploadService;

    private final CheckService checkService;

    @Transactional(readOnly = true)
    public UserResponseDTO<UserGetResponseDTO> getUserByToken(String token) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        UserGetResponseDTO userGetResponseDTO = UserGetResponseDTO.from(user);
        UserResponseDTO<UserGetResponseDTO> userResponseDTO = new UserResponseDTO<>(userGetResponseDTO);

        return userResponseDTO;
    }

    @Transactional
    public UserResponseDTO<UserUpdateResponseDTO> updateUserImageUrl(
        String token,
        UserProfileUpdateRequestDTO userProfileUpdateRequestDTO
    ) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        String imageUrl = userProfileUpdateRequestDTO.imageUrl();

        if (user.getImageUrl() != null) {
            String url = user.getImageUrl();
            s3FileUploadService.deleteImage(url);
        }

        user.updateImageUrl(imageUrl);
        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.from(user);

        return new UserResponseDTO<>(userUpdateResponseDTO);
    }

    @Transactional
    public UserResponseDTO<UserUpdateResponseDTO> updateUserNickname(
        String token,
        @Valid UserNicknameUpdateRequestDTO userUpdateRequestDTO
    ) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        String updateNickname = userUpdateRequestDTO.nickname();
        user.updateNickname(updateNickname);

        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.from(user);

        return new UserResponseDTO<>(userUpdateResponseDTO);
    }
}
