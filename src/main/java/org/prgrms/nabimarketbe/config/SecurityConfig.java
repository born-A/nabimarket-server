package org.prgrms.nabimarketbe.config;

import org.prgrms.nabimarketbe.member.oauth2.handler.OAuth2LoginSuccessHandler;
import org.prgrms.nabimarketbe.member.oauth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.formLogin().disable() // FormLogin 사용 X
			.httpBasic().disable() // httpBasic 사용 X
			.csrf().disable() // csrf 보안 사용 X
			.headers().frameOptions().disable()
			.and()
			// 세션 사용하지 않으므로 STATELESS로 설정
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			//== URL별 권한 관리 옵션 ==//
			.authorizeRequests()

			// 아이콘, css, js 관련
			// 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능, h2-console에 접근 가능
			.antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
			.antMatchers("/sign-up").permitAll() // 회원가입 접근 가능
			.anyRequest().permitAll()
			.and()
			//== 소셜 로그인 설정 ==//
			.oauth2Login()
			.loginPage("/api/v1/users/oauth2/authorize/google/login")
			.permitAll()
			.defaultSuccessUrl("/home")
			.successHandler(oAuth2LoginSuccessHandler)
			.userInfoEndpoint()
			.userService(customOAuth2UserService); // customUserService 설정

		// 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
		// 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
		// 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
