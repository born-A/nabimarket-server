package org.prgrms.nabimarketbe.oauth2.google.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.prgrms.nabimarketbe.domain.user.dto.UserLoginResponseDTO;
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

	@GetMapping("/login")
	public void socialLogin(
		HttpServletResponse response
	) throws IOException {
		String requestURL = oauthService.requestRedirectUrl();

		response.sendRedirect(requestURL);
	}

	@GetMapping("/redirect")
	public ResponseEntity<UserLoginResponseDTO> callback(
		@RequestParam(name = "code") String code
	) throws JsonProcessingException {
		UserLoginResponseDTO loginResponseDTO = oauthService.OAuth2Login(code);

		return ResponseEntity.ok(loginResponseDTO);
	}
}
