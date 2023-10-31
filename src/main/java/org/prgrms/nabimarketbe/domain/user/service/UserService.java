package org.prgrms.nabimarketbe.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.prgrms.nabimarketbe.domain.user.dto.UserRequestDto;
import org.prgrms.nabimarketbe.domain.user.dto.UserResponseDto;
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
    private UserRepository userJpaRepo;

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userJpaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByNickName(String nickname) {
        User user = userJpaRepo.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUser() {
        return userJpaRepo.findAll()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, UserRequestDto userRequestDto) {
        User modifiedUser = userJpaRepo
                .findById(id).orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));
        modifiedUser.updateNickname(userRequestDto.getNickName());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        userJpaRepo.deleteById(id);
    }
}
