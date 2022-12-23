package com.snowballer.api.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.User;
import com.snowballer.api.repository.TownRepository;
import com.snowballer.api.repository.UserRepository;
import com.snowballer.api.service.UrlService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLoginAuthHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final UrlService urlService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication)
		throws IOException, ServletException {
		String token = jwtTokenProvider.createAccessToken(authentication);

		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.addHeader("Authorization", "Bearer " + token);

		CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
		String userId = user.getName();

		String targetUrl = urlService.getTownUrl(Long.valueOf(userId));
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
