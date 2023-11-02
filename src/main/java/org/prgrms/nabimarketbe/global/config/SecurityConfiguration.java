package org.prgrms.nabimarketbe.global.config;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.global.security.handler.CustomAccessDeniedHandler;
import org.prgrms.nabimarketbe.global.security.handler.CustomAuthenticationEntryPoint;
import org.prgrms.nabimarketbe.global.security.jwt.filter.JwtAuthenticationFilter;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
    private final JwtProvider jwtProvider;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .csrf().disable()
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests -> authorizeRequests
                    .antMatchers(HttpMethod.POST, "/v1/signup", "/v1/login",
                            "/v1/reissue", "/v1/social/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/users/oauth2/authorize/google/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/users/oauth2/authorize/kakao/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
                    .anyRequest().hasRole("USER"))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v2/api-docs", "/webjars/**", "/swagger/**", "/h2-console/**");
    }
}
