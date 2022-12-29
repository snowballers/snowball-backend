package com.snowballer.api.security;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
	static private String secretKey = "snowballer-snowman";

	private long tokenValidTime = 24 * 60 * 60 * 1000L;
	private final String AUTHORITIES_KEY = "role";

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createAccessToken(Authentication authentication) {
		Date now = new Date();
		CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
		String userId = user.getName();
		String role = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		return Jwts.builder()
			.signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
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
			.signWith(SignatureAlgorithm.HS512, secretKey)
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		CustomUserDetails principal = new CustomUserDetails(Long.valueOf(claims.getSubject()), authorities);
		return new OAuth2AuthenticationToken(principal, authorities, "id");
		//return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	// 토큰에서 회원 정보 추출
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getSubject();
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
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

}
