package com.snowballer.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import com.snowballer.api.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		//헤더에서 JWT를 받아옵니다.
		//String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		String token = jwtTokenProvider
			.resolveHeaderToken((HttpServletRequest)request);
		//유효한 토큰인지 확인합니다.
		if (token != null && jwtTokenProvider.validateToken(token)) {
			// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			// SecurityContext 에 Authentication 객체를 저장합니다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println("token value " + token);

			HttpServletResponse httpServletResponse = (HttpServletResponse)response;
			httpServletResponse.addHeader("Authorization", "Bearer " + token);
		}
		chain.doFilter(request, response);
	}
}
