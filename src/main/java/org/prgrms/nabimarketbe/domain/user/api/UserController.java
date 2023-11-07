package org.prgrms.nabimarketbe.domain.user.api;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserGetResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public SingleResult<UserResponseDTO> getUserByToken(@RequestHeader(name = "authorization") String token) {
        UserGetResponseDTO userGetResponseDTO = userService.getUserByToken(token);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userGetResponseDTO);

        return ResponseFactory.getSingleResult(userResponseDTO);
    }

    @PutMapping("/profile-image")
    public SingleResult<UserResponseDTO> updateUserImageUrl(
        @RequestHeader(name = "authorization") String token,
        @RequestPart("file") MultipartFile file
    ) {
        UserUpdateResponseDTO userUpdateResponseDTO = userService.updateUserImageUrl(token, file);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userUpdateResponseDTO);

        return ResponseFactory.getSingleResult(userResponseDTO);
    }

    @PutMapping("/nickname")
    public SingleResult<UserResponseDTO> updateUserNickname(
        @RequestHeader(name = "authorization") String token,
        @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserUpdateResponseDTO userUpdateResponseDTO = userService.updateUserNickname(token, userUpdateRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userUpdateResponseDTO);

        return ResponseFactory.getSingleResult(userResponseDTO);
    }
}
