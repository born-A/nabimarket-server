package org.prgrms.nabimarketbe.member.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.prgrms.nabimarketbe.member.application.OAuth2Service;
import org.prgrms.nabimarketbe.member.domain.GetSocialOAuth2Res;
import org.springframework.http.HttpStatus;
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
@RequestMapping
public class OAuth2Controller {

	private final OAuth2Service oauthService;

	@GetMapping("/auth/{type}")
	public void socialLogin(
		@PathVariable("type") String type,
		HttpServletResponse response
	) throws IOException {
		String requestURL = oauthService.request(type.toUpperCase());
		response.sendRedirect(requestURL);
	}

	@GetMapping("/authorize/{type}/sign-up")
	public void socialSignUp(
		@PathVariable("type") String type,
		HttpServletResponse response
	) {

	}

	@GetMapping("/auth/{type}/redirect")
	public ResponseEntity<?> callback(
		@PathVariable(name = "type") String type,
		@RequestParam(name = "code") String code
	) throws JsonProcessingException {
		log.info("redirected");
		GetSocialOAuth2Res getSocialOAuth2Res = oauthService.oAuthLogin(code);
		return new ResponseEntity<>(getSocialOAuth2Res, HttpStatus.OK);
	}

	@GetMapping("/test")
	public String test() {
		return "index";
	}
}
