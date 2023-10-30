package org.prgrms.nabimarketbe.member.domain;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuth2 {

	String getOAuth2RedirectURL();

	ResponseEntity<String> requestAccessToken(String code);

	GoogleOAuth2Token getAccessToken(ResponseEntity<String> accessToken) throws JsonProcessingException;

	ResponseEntity<String> requestUserInfo(GoogleOAuth2Token googleOAuthToken);

	GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException;

}
