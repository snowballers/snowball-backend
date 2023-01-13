package com.snowballer.api.security;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {
	private final String secret;
	private Key key;

	private long tokenValidTime = 24 * 60 * 60 * 1000L;
	private final String AUTHORITIES_KEY = "role";

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secret) {
		this.secret = secret;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createAccessToken(Authentication authentication) {
		Date now = new Date();
		CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
		String userId = user.getName();
		String role = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		return Jwts.builder()
			.signWith(key, SignatureAlgorithm.HS512)
			.setSubject(userId)
			.claim(AUTHORITIES_KEY, role)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.compact();
	}

	public String createAccessToken(String payload) {
		Date now = new Date();
		Claims claims = Jwts.claims().setSubject(payload);
		return Jwts.builder()
			.signWith(key, SignatureAlgorithm.HS512)
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		String token = accessToken.substring(7);
		Claims claims = parseClaims(token);

		// Collection<? extends GrantedAuthority> authorities =
		// 	Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
		// 		.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		CustomUserDetails principal = new CustomUserDetails(Long.valueOf(claims.getSubject()), null);
		return new OAuth2AuthenticationToken(principal, null, "id");
		//return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	private Claims parseClaims(String accessToken) {
		try {
			System.out.println(accessToken);
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	// 토큰에서 회원 정보 추출
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
	}

	// Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
	public String resolveToken(HttpServletRequest request) {
		return request.getParameter("token");
	}

	public String resolveHeaderToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			String token = jwtToken.substring(7);
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;

	}
}
