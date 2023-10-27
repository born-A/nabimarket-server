package org.prgrms.nabimarketbe.user.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.global.model.CommonResult;
import org.prgrms.nabimarketbe.global.model.ListResult;
import org.prgrms.nabimarketbe.global.model.SingleResult;
import org.prgrms.nabimarketbe.oauth.service.response.ResponseService;
import org.prgrms.nabimarketbe.user.dto.UserRequestDto;
import org.prgrms.nabimarketbe.user.dto.UserResponseDto;
import org.prgrms.nabimarketbe.user.service.UserService;
import org.springframework.web.bind.annotation.*;

//@PreAuthorize("hasRole('ROLE_USER') and hasAnyRole('ROLE_IORN', 'ROLE_SILVER', 'ROLE_GOLD', 'ROLE_BRONZE')")
@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    //@PreAuthorize("hasRole('ROLE_PLATINUM') and hasRole('ROLE_GOLD')")
    @ApiOperation(value = "회원 단건 검색", notes = "userId로 회원을 조회합니다.")
    @GetMapping("/user/id/{userId}")
    public SingleResult<UserResponseDto> findUserById
            (@ApiParam(value = "회원 ID", required = true) @PathVariable Long userId,
             @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(userService.findById(userId));
    }

    @GetMapping("/user/nickname/{nickname}")
    public SingleResult<UserResponseDto> findUserByNickName(
            @PathVariable String nickname,
            @RequestParam String lang
    ) {
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
