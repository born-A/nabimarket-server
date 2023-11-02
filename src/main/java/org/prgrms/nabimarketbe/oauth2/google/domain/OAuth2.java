package org.prgrms.nabimarketbe.oauth2.google.domain;

import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleOAuth2TokenDTO;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleUserInfoDTO;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuth2 {
	String getOAuth2RedirectUrl();

	ResponseEntity<String> requestAccessToken(String code);

	GoogleOAuth2TokenDTO getAccessToken(ResponseEntity<String> accessToken) throws JsonProcessingException;

	ResponseEntity<String> requestUserInfo(GoogleOAuth2TokenDTO googleOAuthToken);

	GoogleUserInfoDTO parseUserInfo(ResponseEntity<String> userInfo) throws JsonProcessingException;
}
