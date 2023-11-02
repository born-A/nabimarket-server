package org.prgrms.nabimarketbe.domain.user.api;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.ListResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public SingleResult<UserResponseDTO> findUserById(@PathVariable Long userId) {
        return ResponseFactory.getSingleResult(userService.findById(userId));
    }

    @GetMapping("/{nickname}")
    public SingleResult<UserResponseDTO> findUserByNickName(@PathVariable String nickname) {
        return ResponseFactory.getSingleResult(userService.findByNickname(nickname));
    }

    @GetMapping
    public ListResult<UserResponseDTO> findAllUser() {
        return ResponseFactory.getListResult(userService.findAllUser());
    }

    @PutMapping
    public SingleResult<Long> update (
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
