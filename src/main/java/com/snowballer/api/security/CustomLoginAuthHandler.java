package com.snowballer.api.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLoginAuthHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication)
		throws IOException, ServletException {
		//DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
		//String token = jwtTokenProvider.createToken((String) oAuth2User.getAttributes().get("login"));
		String token = jwtTokenProvider.createAccessToken(authentication);

		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.addHeader("Authorization", "Bearer " + token);

        /*
        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .queryParam("token", token)
                .build().toUriString();

         */

		// TODO 타겟 url 설정 -> 자기의 마을 해쉬값 url 을 여기다 넣어줘야겠네
		String targetUrl = UriComponentsBuilder.fromUriString("/")
			.build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
