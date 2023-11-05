package org.prgrms.nabimarketbe.domain.user.api;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.ListResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public SingleResult<UserResponseDTO> getUserById(@PathVariable Long userId) {
        return ResponseFactory.getSingleResult(userService.findById(userId));
    }

    @GetMapping("/{nickname}")
    public SingleResult<UserResponseDTO> getUserByNickName(@PathVariable String nickname) {
        return ResponseFactory.getSingleResult(userService.findByNickname(nickname));
    }

    @GetMapping
    public ListResult<UserResponseDTO> getUsers() {
        return ResponseFactory.getListResult(userService.findAllUser());
    }

    @PostMapping("/image-url")
    public SingleResult<UserResponseDTO>  updateUserImageUrl(
            @RequestParam Long userId,
            @RequestPart("file") MultipartFile file
    ) {
        userService.updateImageUrl(userId, file);

        return ResponseFactory.getSingleResult(userService.findById(userId));
    }

    @PutMapping
    public SingleResult<Long> updateUserNickname (
            @RequestParam Long userId,
            @RequestParam String nickname
    ) {
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .nickName(nickname)
                .build();

        return ResponseFactory.getSingleResult(userService.update(userId, userRequestDTO));
    }

    @DeleteMapping("/{userId}")
    public CommonResult delete(@PathVariable Long userId) {
        userService.delete(userId);

        return ResponseFactory.getSuccessResult();
    }
}
