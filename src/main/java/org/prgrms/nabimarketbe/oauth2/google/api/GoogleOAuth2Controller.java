package org.prgrms.nabimarketbe.oauth2.google.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.prgrms.nabimarketbe.domain.user.dto.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.SignService;
import org.prgrms.nabimarketbe.global.util.model.MyCommonResult;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleUserInfoDTO;
import org.prgrms.nabimarketbe.oauth2.google.service.GoogleOAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/users/oauth2/authorize/google")
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
	public ResponseEntity<MyCommonResult<UserLoginResponseDTO>> callback(
		@RequestParam(name = "code") String code
	) throws JsonProcessingException {
		GoogleUserInfoDTO googleUserInfoDTO = oauthService.OAuth2Login(code);

		UserLoginResponseDTO loginResponseDTO = signService.signIn(googleUserInfoDTO);

		MyCommonResult<UserLoginResponseDTO> response = new MyCommonResult<>(
			"C001",
			"성공했습니다.",
			loginResponseDTO
		);

		return ResponseEntity.ok(response);
	}
}
