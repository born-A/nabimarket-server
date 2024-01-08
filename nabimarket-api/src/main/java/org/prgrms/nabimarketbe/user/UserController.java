package org.prgrms.nabimarketbe.user;

import org.prgrms.nabimarketbe.model.ResponseFactory;
import org.prgrms.nabimarketbe.model.SingleResult;
import org.prgrms.nabimarketbe.jwt.dto.AccessTokenResponseDTO;
import org.prgrms.nabimarketbe.user.dto.request.UserNicknameUpdateRequestDTO;
import org.prgrms.nabimarketbe.user.dto.request.UserProfileUpdateRequestDTO;
import org.prgrms.nabimarketbe.user.dto.response.UserGetResponseDTO;
import org.prgrms.nabimarketbe.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.user.dto.response.UserUpdateResponseDTO;
import org.prgrms.nabimarketbe.user.service.SignService;
import org.prgrms.nabimarketbe.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "유저", description = "유저 관련 API 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final SignService signService;

    @Operation(summary = "validation api")
    @GetMapping
    public ResponseEntity<SingleResult<UserResponseDTO<UserGetResponseDTO>>> getUserByToken(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "authorization") String token
    ) {
        UserResponseDTO<UserGetResponseDTO> userResponseDTO = userService.getUserByToken(token);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(userResponseDTO));
    }

    @Operation(summary = "유저 프로필 이미지 수정", description = "유저는 자신의 프로필 이미지를 수정할 수 있습니다.")
    @PutMapping("/profile-image")
    public ResponseEntity<SingleResult<UserResponseDTO<UserUpdateResponseDTO>>> updateUserImageUrl(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "authorization") String token,
        @Parameter(description = "프로필 이미지의 업로드 url 요청값", required = true)
        @RequestBody UserProfileUpdateRequestDTO userProfileUpdateRequestDTO
    ) {
        UserResponseDTO<UserUpdateResponseDTO> userResponseDTO = userService.updateUserImageUrl(
            token,
            userProfileUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(userResponseDTO));
    }

    @Operation(summary = "유저 닉네임 수정", description = "유저는 자신의 닉네임을 수정할 수 있습니다.")
    @PutMapping("/nickname")
    public ResponseEntity<SingleResult<UserResponseDTO<UserUpdateResponseDTO>>> updateUserNickname(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "authorization") String token,
        @Parameter(description = "유저의 새로운 닉네임 요청값", required = true)
        @RequestBody UserNicknameUpdateRequestDTO userNicknameUpdateRequestDTO
    ) {
        UserResponseDTO<UserUpdateResponseDTO> userResponseDTO = userService.updateUserNickname(
            token,
            userNicknameUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(userResponseDTO));
    }

    @Operation(summary = "access 토큰 재발급", description = "access 토큰이 만료되었을 때 재발급 받을 수 있습니다.")
    @GetMapping("/re-issue")
    public ResponseEntity<SingleResult<AccessTokenResponseDTO>> reissue(
        @Parameter(name = "Authorization", description = "만료 되지 않은 RefreshToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String refreshToken
    ) {
        AccessTokenResponseDTO accessTokenResponseDTO = signService.reissue(refreshToken);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(accessTokenResponseDTO));
    }
}
