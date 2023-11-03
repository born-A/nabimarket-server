package org.prgrms.nabimarketbe.domain.user.service;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final CheckService checkService;

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
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
    public Long update(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User modifiedUser = userRepository
                .findById(id).orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));
        modifiedUser.updateNickname(userUpdateRequestDTO.nickName());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
