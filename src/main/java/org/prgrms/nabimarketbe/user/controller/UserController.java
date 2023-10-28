package org.prgrms.nabimarketbe.user.controller;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.global.model.CommonResult;
import org.prgrms.nabimarketbe.global.model.ListResult;
import org.prgrms.nabimarketbe.global.model.SingleResult;
import org.prgrms.nabimarketbe.oauth.service.response.ResponseService;
import org.prgrms.nabimarketbe.user.dto.UserRequestDto;
import org.prgrms.nabimarketbe.user.dto.UserResponseDto;
import org.prgrms.nabimarketbe.user.service.UserService;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {
    private final UserService userService;

    private final ResponseService responseService;

    @GetMapping("/user/id/{userId}")
    public SingleResult<UserResponseDto> findUserById(@PathVariable Long userId) {
        return responseService.getSingleResult(userService.findById(userId));
    }

    @GetMapping("/user/nickname/{nickname}")
    public SingleResult<UserResponseDto> findUserByNickName(@PathVariable String nickname, @RequestParam String lang) {
        return responseService.getSingleResult(userService.findByNickName(nickname));
    }

    @GetMapping("/users")
    public ListResult<UserResponseDto> findAllUser() {
        return responseService.getListResult(userService.findAllUser());
    }

    @PutMapping("/user")
    public SingleResult<Long> update (
            @RequestParam Long userId,
            @RequestParam String nickName
    ) {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .nickName(nickName)
                .build();

        return responseService.getSingleResult(userService.update(userId, userRequestDto));
    }

    @DeleteMapping("/user/{userId}")
    public CommonResult delete(@PathVariable Long userId) {
        userService.delete(userId);

        return responseService.getSuccessResult();
    }
}
