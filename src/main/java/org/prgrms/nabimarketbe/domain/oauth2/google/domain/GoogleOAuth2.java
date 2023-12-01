package org.prgrms.nabimarketbe.domain.oauth2.google.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.prgrms.nabimarketbe.domain.oauth2.google.dto.GoogleOAuth2TokenDTO;
import org.prgrms.nabimarketbe.domain.oauth2.google.dto.GoogleUserInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2 implements OAuth2 {
    @Value("${spring.security.oauth2.client.registration.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    @Value("${spring.security.oauth2.client.registration.google.token-url}")
    private String GOOGLE_TOKEN_REQUEST_URL;

    @Value("${spring.security.oauth2.client.registration.google.user-info-url}")
    private String GOOGLE_USER_INFO_REQUEST_URL;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Override
    public String getOAuth2RedirectUrl() {
        Map<String, Object> params = new HashMap<>();

        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        return redirectURL;
    }

    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> accessToken = restTemplate.postForEntity(
            GOOGLE_TOKEN_REQUEST_URL,
            params,
            String.class
        );

        return accessToken;
    }

    @Override
    public GoogleOAuth2TokenDTO getAccessToken(ResponseEntity<String> accessToken) throws JsonProcessingException {
        return objectMapper.readValue(accessToken.getBody(), GoogleOAuth2TokenDTO.class);
    }

    @Override
    public ResponseEntity<String> requestUserInfo(GoogleOAuth2TokenDTO googleOAuthToken) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        headers.add("Authorization", "Bearer " + googleOAuthToken.accessToken());
        ResponseEntity<String> exchange = restTemplate.exchange(
            GOOGLE_USER_INFO_REQUEST_URL,
            HttpMethod.GET,
            request,
            String.class
        );

        return exchange;
    }

    @Override
    public GoogleUserInfoDTO parseUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
        GoogleUserInfoDTO googleUserInfoDTO = objectMapper.readValue(
            userInfoResponse.getBody(),
            GoogleUserInfoDTO.class
        );

        return googleUserInfoDTO;
    }
}
