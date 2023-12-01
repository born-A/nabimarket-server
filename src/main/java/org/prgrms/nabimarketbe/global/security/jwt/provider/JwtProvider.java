package org.prgrms.nabimarketbe.global.security.jwt.provider;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.prgrms.nabimarketbe.global.security.entity.RefreshToken;
import org.prgrms.nabimarketbe.global.security.jwt.dto.AccessTokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("spring.jwt.secret")
    private String secretKey;

    private String ROLE = "role";

    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; // 1 hour

    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L; // 14 day

    private final UserDetailsService userDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        // 암호화
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Transactional
    public AccessTokenResponseDTO createAccessTokenDTO(Long userPk, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put(ROLE, role);

        Date now = new Date();

        String accessToken = Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        return new AccessTokenResponseDTO(accessToken);
    }

    // Jwt 생성
    @Transactional
    public TokenResponseDTO createTokenDTO(Long userPk, String role) {
        // Claims 에 user 구분을 위한 User pk 및 authorities 목록 삽입
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put(ROLE, role);

        // 생성날짜, 만료날짜를 위한 Date
        Date now = new Date();

        String accessToken = Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        String refreshToken = Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

        RefreshToken refreshTokenEntity = new RefreshToken(userPk, refreshToken);

        Optional<RefreshToken> refreshTokenByUserId = refreshTokenRepository.findByUserId(userPk);

        refreshTokenByUserId.ifPresentOrElse(
            refreshToken2 -> refreshToken2.updateToken(refreshToken),
            () -> refreshTokenRepository.save(refreshTokenEntity)
        );

        return TokenResponseDTO.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpireDate(accessTokenValidMillisecond)
            .build();
    }

    // Jwt 로 인증정보를 조회
    public Authentication getAuthentication(String token) {
        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLE) == null) {
            throw new RuntimeException("AuthenticationEntryPointException");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "Authorization: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Long parseUserId(String token) {
        Claims claims = parseClaims(token);

        return Long.valueOf(claims.getSubject());
    }

    // jwt 의 유효성 및 만료일자 확인
    public void validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
            throw new JwtException(ErrorCode.USER_TOKEN_NOT_VALID.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
            throw new JwtException(ErrorCode.USER_TOKEN_EXPIRED.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
            throw new JwtException(ErrorCode.USER_TOKEN_NOT_SUPPORTED.getMessage());
        } catch (Exception e) {
            log.error("잘못된 토큰입니다.");
            throw new JwtException(ErrorCode.USER_TOKEN_NOT_VALID.getMessage());
        }
    }
}
