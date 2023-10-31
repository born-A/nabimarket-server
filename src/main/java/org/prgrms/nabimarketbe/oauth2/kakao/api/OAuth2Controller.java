package org.prgrms.nabimarketbe.oauth2.kakao.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenDto;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.domain.user.service.SignService;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.oauth2.kakao.service.OAuth2Service;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class OAuth2Controller {
    private final RestTemplate restTemplate;

    private final Environment env;

    private final OAuth2Service OAuth2Service;

    private final SignService signService;

    private final ResponseFactory responseFactory;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;

    @GetMapping("/login")
    public void socialLogin(HttpServletResponse response) throws IOException {
        StringBuilder loginUri = new StringBuilder()
                .append(env.getProperty("social.kakao.url.login"))
                .append("?response_type=code")
                .append("&client_id=").append(kakaoClientId)
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirectUri);

        response.sendRedirect(String.valueOf(loginUri));
    }

    @GetMapping(value = "/redirect")
    public CommonResult redirectKakao(
            ModelAndView mav,
            @RequestParam String code) {
        //받은 token info 에서 acccess token 추출
        String accessToken = OAuth2Service.getKakaoTokenInfo(code).getAccess_token();

        log.info(accessToken);

        return signService.signupBySocial(accessToken);
    }

    @GetMapping(value = "/unlink")
    public CommonResult unlinkKakao(@RequestParam String accessToken) {
        String unlinkUri = env.getProperty("social.kakao.url.unlink");
        if (unlinkUri == null) throw new RuntimeException("CommunicationException");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(unlinkUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("unlink " + response.getBody());

            return responseFactory.getSuccessResult();
        }

        throw new RuntimeException("CommunicationException");
    }

    //엑세스 토큰 만료시 회원 검증 후 리프레쉬 토큰을 검증해서 액세스 토큰과 리프레시 토큰을 재발급
    @PostMapping("/reissue")
    public SingleResult<TokenDto> reissue(
            @RequestBody TokenRequestDto tokenRequestDto) {
        return responseFactory.getSingleResult(signService.reissue(tokenRequestDto));
    }
}
