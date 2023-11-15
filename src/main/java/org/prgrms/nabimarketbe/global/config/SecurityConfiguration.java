package org.prgrms.nabimarketbe.global.config;

import org.prgrms.nabimarketbe.domain.user.Role;
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

import lombok.RequiredArgsConstructor;

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
                    .antMatchers(HttpMethod.GET, "/api/v1/users/oauth2/authorize/google/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/users/oauth2/authorize/kakao/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/complete-requests/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/cards/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/suggestions/**").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/index.html","/swagger-ui/**","/v3/api-docs/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
                    .anyRequest().hasRole(Role.USER.toString()))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
       return (web) -> web.ignoring().
        antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/favicon.ico", "/error","/h2-console/**");
    }
}
