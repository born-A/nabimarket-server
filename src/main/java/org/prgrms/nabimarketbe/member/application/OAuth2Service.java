package org.prgrms.nabimarketbe.member.application;

import org.prgrms.nabimarketbe.member.domain.GetSocialOAuth2Res;
import org.prgrms.nabimarketbe.member.domain.GoogleOAuth2Token;
import org.prgrms.nabimarketbe.member.domain.GoogleUser;
import org.prgrms.nabimarketbe.member.domain.OAuth2;
import org.prgrms.nabimarketbe.member.jwt.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final OAuth2 oAuth2;

	private final JwtService jwtService;

	public String request(String type) {
		String redirectURL;
		redirectURL = oAuth2.getOAuth2RedirectURL();
		return redirectURL;
	}

	public GetSocialOAuth2Res oAuthLogin(String code) throws JsonProcessingException {
		ResponseEntity<String> accessToken = oAuth2.requestAccessToken(code);
		GoogleOAuth2Token googleOAuth2Token = oAuth2.getAccessToken(accessToken);

		ResponseEntity<String> userInfoResponse= oAuth2.requestUserInfo(googleOAuth2Token);

		GoogleUser googleUser = oAuth2.getUserInfo(userInfoResponse);

		String user_id = googleUser.email();

		return new GetSocialOAuth2Res("1234",1,"asdf", googleOAuth2Token.tokenType());
	}

}
