package org.prgrms.nabimarketbe.oauth2.kakao.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.prgrms.nabimarketbe.domain.user.dto.request.SocialUserInfoDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.service.SignService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.prgrms.nabimarketbe.oauth2.kakao.dto.KakaoProfile;
import org.prgrms.nabimarketbe.oauth2.kakao.service.OAuth2Service;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "카카오 소셜 로그인", description = "소셜 로그인(카카오)")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/oauth2/authorize/kakao")
public class OAuth2Controller {
    private final RestTemplate restTemplate;

    private final Environment env;

    private final OAuth2Service OAuth2Service;

    private final SignService signService;

    @GetMapping("/login")
    public void socialLogin(HttpServletResponse response) throws IOException {
        StringBuilder loginUri = OAuth2Service.createUri();
        response.sendRedirect(String.valueOf(loginUri));
    }

    @GetMapping(value = "/redirect")
    public ResponseEntity<SingleResult<UserLoginResponseDTO>> redirectKakao(@RequestParam String code) {
        KakaoProfile profile = OAuth2Service.getResultProfile(code);
        if (profile == null)
            throw new RuntimeException("카카오에 해당 회원이 없습니다.");
        
        SocialUserInfoDTO socialUserInfoDTO = SocialUserInfoDTO.builder()
            .accountId(profile.getId())
            .provider("KAKAO")
            .build();

        UserLoginResponseDTO userLoginResponseDTO = signService.signIn(socialUserInfoDTO);
        SingleResult<UserLoginResponseDTO> response = ResponseFactory.getSingleResult(userLoginResponseDTO);

        return ResponseEntity.ok(response);
    }

    //TODO : 사용자가 accessToken 넘기는건 아닌거 같음
    @GetMapping(value = "/unlink")
    public CommonResult unlinkKakao(@RequestParam String accessToken) {
        String unlinkUri = env.getProperty("social.kakao.url.unlink");
        if (unlinkUri == null)
            throw new RuntimeException("CommunicationException");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(unlinkUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("unlink " + response.getBody());

            return ResponseFactory.getSuccessResult();
        }

        throw new RuntimeException("CommunicationException");
    }
}
