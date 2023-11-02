package org.prgrms.nabimarketbe.global.security.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;

    // request 로 들어오는 Jwt 의 유효성을 검증 - JwtProvider.validationToken() 을 필터로서 FilterChain 에 추가
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        // request 에서 token 을 취한다.
        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        // 검증
        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        if (token != null && jwtProvider.validationToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
