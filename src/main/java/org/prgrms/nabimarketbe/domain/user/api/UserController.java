package org.prgrms.nabimarketbe.domain.user.api;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.UserService;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.ListResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final ResponseFactory responseFactory;

    @GetMapping("/{userId}")
    public SingleResult<UserResponseDTO> findUserById(@PathVariable Long userId) {
        return responseFactory.getSingleResult(userService.findById(userId));
    }

    @GetMapping("/{nickname}")
    public SingleResult<UserResponseDTO> findUserByNickName(@PathVariable String nickname) {
        return responseFactory.getSingleResult(userService.findByNickname(nickname));
    }

    @GetMapping
    public ListResult<UserResponseDTO> findAllUser() {
        return responseFactory.getListResult(userService.findAllUser());
    }

    @PutMapping
    public SingleResult<Long> update (
            @RequestParam Long userId,
            @RequestParam String nickname
    ) {
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .nickName(nickname)
                .build();

        return responseFactory.getSingleResult(userService.update(userId, userRequestDTO));
    }

    @DeleteMapping("/{userId}")
    public CommonResult delete(@PathVariable Long userId) {
        userService.delete(userId);

        return responseFactory.getSuccessResult();
    }
}
