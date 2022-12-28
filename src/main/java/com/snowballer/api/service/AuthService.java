package com.snowballer.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.LoginProviderType;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.User;
import com.snowballer.api.domain.UserState;
import com.snowballer.api.dto.request.LoginRequest;
import com.snowballer.api.dto.response.LoginResponse;
import com.snowballer.api.repository.UserRepository;
import com.snowballer.api.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoService kakaoService;

	private final TownService townService;
	private final UrlService urlService;

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public LoginResponse login(LoginRequest request) {

		LoginProviderType providerType = LoginProviderType.valueOf(request.getProvider().toUpperCase());
		Map<String, Object> loginUserInfo = new HashMap<>();

		String socialLoginId = null;
		String name = null;

		if (LoginProviderType.KAKAO.equals(providerType)) {
			loginUserInfo = kakaoService.login(request.getCode());
		} else if (LoginProviderType.GOOGLE.equals(providerType)) {
			// TODO GOOGLE
		} else {
			throw new RestApiException(ErrorCode.DEFAULT_ERROR_CODE);
		}

		socialLoginId = loginUserInfo.get("id").toString();
		name = loginUserInfo.get("name").toString();

		Optional<User> user = userRepository.findBySocialLoginIdAndState(socialLoginId, UserState.ACTIVE);
		User loginUser;
		String townUrl = null;

		if (user.isEmpty()) {
			loginUser = createSocialUser(socialLoginId, name, providerType);
			townUrl = townService.createTown(userRepository.save(loginUser));
		} else {
			loginUser = user.get();
			Town town = loginUser.getTownList().get(0);
			townUrl = urlService.encoding(town.getId());
		}

		String token = jwtTokenProvider.createAccessToken(String.valueOf(loginUser.getId()));

		return LoginResponse.builder()
			.jwt(token)
			.townUrl(townUrl)
			.build();
	}

	private User createSocialUser(String socialLoginId, String name, LoginProviderType providerType) {
		User createUser = User.builder()
			.nickname(name)
			.socialLoginId(socialLoginId)
			.providerType(providerType)
			.build();
		return createUser;
	}
}
