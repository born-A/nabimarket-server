package org.prgrms.nabimarketbe.member.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.prgrms.nabimarketbe.member.application.OAuth2Service;
import org.prgrms.nabimarketbe.member.domain.LoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class OAuth2Controller {

	private final OAuth2Service oauthService;

	@GetMapping("/login")
	public void socialLogin(
		HttpServletResponse response
	) throws IOException {
		String requestURL = oauthService.request();
		response.sendRedirect("/oauth2/authorization/google");
		// response.sendRedirect(requestURL);
	}

	@GetMapping("/home")
	public String socialSignUp(
		@PathVariable("type") String type,
		HttpServletResponse response
	) {
		return "index";
	}

	@GetMapping("/redirect")
	public ResponseEntity<?> callback(
		@RequestParam(name = "code") String code
	) throws JsonProcessingException {
		log.info("redirected!!");
		LoginResponseDTO loginResponseDTO = oauthService.oAuthLogin(code);
		return ResponseEntity.ok(loginResponseDTO);
	}

}
