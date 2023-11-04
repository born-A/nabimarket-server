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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    private S3FileUploadService s3FileUploadService;

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return UserResponseDTO.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByAccountId(String accountId) {
        User user = userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return UserResponseDTO.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return UserResponseDTO.from(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, UserRequestDTO userRequestDTO) {
        User modifiedUser = userRepository
                .findById(id).orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        modifiedUser.updateNickname(userRequestDTO.getNickName());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDTO updateImageUrl(Long userId, MultipartFile file) {
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
