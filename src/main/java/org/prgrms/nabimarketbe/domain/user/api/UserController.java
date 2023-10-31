package org.prgrms.nabimarketbe.domain.user.api;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.user.dto.UserRequestDto;
import org.prgrms.nabimarketbe.domain.user.dto.UserResponseDto;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.ListResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {
    private final UserService userService;

    private final ResponseFactory responseFactory;

    @GetMapping("/user/id/{userId}")
    public SingleResult<UserResponseDto> findUserById(@PathVariable Long userId) {
        return responseFactory.getSingleResult(userService.findById(userId));
    }

    @GetMapping("/user/nickname/{nickname}")
    public SingleResult<UserResponseDto> findUserByNickName(@PathVariable String nickname) {
        return responseFactory.getSingleResult(userService.findByNickName(nickname));
    }

    @GetMapping("/users")
    public ListResult<UserResponseDto> findAllUser() {
        return responseFactory.getListResult(userService.findAllUser());
    }

    @PutMapping("/user")
    public SingleResult<Long> update (
            @RequestParam Long userId,
            @RequestParam String nickname
    ) {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .nickName(nickname)
                .build();

        return responseFactory.getSingleResult(userService.update(userId, userRequestDto));
    }

    @DeleteMapping("/user/{userId}")
    public CommonResult delete(@PathVariable Long userId) {
        userService.delete(userId);

        return responseFactory.getSuccessResult();
    }
}
