package org.prgrms.nabimarketbe.oauth2.google.domain;

import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleOAuth2Token;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleUser;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuth2 {

	String getOAuth2RedirectUrl();

	ResponseEntity<String> requestAccessToken(String code);

	GoogleOAuth2Token getAccessToken(ResponseEntity<String> accessToken) throws JsonProcessingException;

	ResponseEntity<String> requestUserInfo(GoogleOAuth2Token googleOAuthToken);

	GoogleUser parseUserInfo(ResponseEntity<String> userInfo) throws JsonProcessingException;

}
