package org.prgrms.nabimarketbe.member.oauth2.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.prgrms.nabimarketbe.member.dao.UserRepository;
import org.prgrms.nabimarketbe.member.domain.User;
import org.prgrms.nabimarketbe.member.jwt.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final RequestCache requestCache = new HttpSessionRequestCache();

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private final JwtService jwtService;

    private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		log.info("OAuth2 Login 성공!");
		try {

			SavedRequest savedRequest = requestCache.getRequest(request, response);

			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			User user = userRepository.findByEmail(oAuth2User.getAttribute("email"))
				.orElseThrow(() -> new RuntimeException("커스텀 에러로 바꿔주기"));
			loginSuccess(response, user);
			if(savedRequest != null){
				String targetUrl = savedRequest.getRedirectUrl();
				redirectStrategy.sendRedirect(request, response, targetUrl);
			}else{
				redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
			}
			// TODO : 유저 리프레시토큰 등록하기
		} catch (Exception e) {
			throw e;
		}

	}

	// TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
	private void loginSuccess(HttpServletResponse response, User user) {
		String accessToken = jwtService.createAccessToken(user.getEmail());
		String refreshToken = jwtService.createRefreshToken();

		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

		// jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(user.getEmail(), refreshToken);
	}

}
