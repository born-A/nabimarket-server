package org.prgrms.nabimarketbe.oauth2.google.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.prgrms.nabimarketbe.domain.user.dto.request.SocialUserInfoDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.SignService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.prgrms.nabimarketbe.oauth2.google.service.GoogleOAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "구글 소셜 로그인", description = "소셜 로그인(구글)")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users/oauth2/authorize/google")
public class GoogleOAuth2Controller {
    private final GoogleOAuth2Service oauthService;

    private final SignService signService;

    @GetMapping("/login")
    public void socialLogin(
        HttpServletResponse response
    ) throws IOException {
        String requestURL = oauthService.requestRedirectUrl();

        response.sendRedirect(requestURL);
    }

    @GetMapping("/redirect")
    public ResponseEntity<SingleResult<UserLoginResponseDTO>> callback(
        @RequestParam(name = "code") String code
    ) throws JsonProcessingException {
        SocialUserInfoDTO socialUserInfoDTO = oauthService.oAuth2Login(code);

        UserLoginResponseDTO loginResponseDTO = signService.signIn(socialUserInfoDTO);

        SingleResult<UserLoginResponseDTO> response = ResponseFactory.getSingleResult(loginResponseDTO);

        return ResponseEntity.ok(response);
    }
}
