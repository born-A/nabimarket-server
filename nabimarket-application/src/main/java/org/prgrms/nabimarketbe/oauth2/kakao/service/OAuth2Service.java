package org.prgrms.nabimarketbe.oauth2.kakao.service;

import org.prgrms.nabimarketbe.oauth2.kakao.dto.KakaoProfile;
import org.prgrms.nabimarketbe.oauth2.kakao.dto.RetKakaoOAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2Service {
    private final Environment env;

    private final RestTemplate restTemplate;

    private final Gson gson;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;

    public StringBuilder createUri() {
        StringBuilder loginUri = new StringBuilder()
            .append(env.getProperty("social.kakao.url.login"))
            .append("?response_type=code")
            .append("&client_id=").append(kakaoClientId)
            .append("&redirect_uri=").append(kakaoRedirectUri);

        return loginUri;
    }

    public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        String requestUrl = env.getProperty("social.kakao.url.profile");

        if (requestUrl == null)
            throw new RuntimeException("CommunicationException");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException("CommunicationException");
        }
        throw new RuntimeException("CommunicationException");
    }

    public RetKakaoOAuth getKakaoTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        String requestUri = env.getProperty("social.kakao.url.token");
        if (requestUri == null)
            throw new RuntimeException("CommunicationException");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(requestUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK)
            return gson.fromJson(response.getBody(), RetKakaoOAuth.class);

        throw new RuntimeException("CommunicationException");
    }

    public KakaoProfile getResultProfile(String code) {
        return getKakaoProfile(getKakaoTokenInfo(code).getAccess_token());
    }
}
