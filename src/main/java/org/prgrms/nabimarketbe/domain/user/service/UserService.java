package org.prgrms.nabimarketbe.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.Domain;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
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
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return UserResponseDTO.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByToken(String token) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return UserResponseDTO.from(user);
    }

    @Transactional
    public UserResponseDTO updateUserImageUrl(Long userId, MultipartFile file) {
        User modifiedUser = userRepository
                .findById(userId).orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        if (modifiedUser.getImageUrl() != null) {
            String imageUrl = modifiedUser.getImageUrl();
            s3FileUploadService.deleteImage(imageUrl);
        }

        String url = s3FileUploadService.uploadFile(Domain.USER.name(), userId, file);

        modifiedUser.updateImageUrl(url);

        return UserResponseDTO.from(modifiedUser);
    }
}
