package org.prgrms.nabimarketbe.domain.user.api;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserNicknameUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserProfileUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserGetResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SingleResult<UserResponseDTO<UserGetResponseDTO>>> getUserByToken(
        @RequestHeader(name = "authorization") String token) {
        UserResponseDTO<UserGetResponseDTO> userResponseDTO = userService.getUserByToken(token);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(userResponseDTO));
    }

    @PutMapping("/profile-image")
    public ResponseEntity<SingleResult<UserResponseDTO<UserUpdateResponseDTO>>> updateUserImageUrl(
        @RequestHeader(name = "authorization") String token,
        @RequestBody UserProfileUpdateRequestDTO userProfileUpdateRequestDTO
    ) {
        UserResponseDTO<UserUpdateResponseDTO> userResponseDTO = userService.updateUserImageUrl(
            token,
            userProfileUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(userResponseDTO));
    }

    @PutMapping("/nickname")
    public ResponseEntity<SingleResult<UserResponseDTO<UserUpdateResponseDTO>>> updateUserNickname(
        @RequestHeader(name = "authorization") String token,
        @RequestBody UserNicknameUpdateRequestDTO userNicknameUpdateRequestDTO
    ) {
        UserResponseDTO<UserUpdateResponseDTO> userResponseDTO = userService.updateUserNickname(
            token,
            userNicknameUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(userResponseDTO));
    }
}
