package com.snowballer.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.LoginProviderType;
import com.snowballer.api.domain.User;
import com.snowballer.api.domain.UserState;
import com.snowballer.api.dto.response.LoginResponse;
import com.snowballer.api.repository.UserRepository;
import com.snowballer.api.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoService kakaoService;

	private final TownService townService;

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public LoginResponse login(String provider, String code) {

		LoginProviderType providerType = LoginProviderType.valueOf(provider.toUpperCase());
		Map<String, Object> loginUserInfo = new HashMap<>();

		String socialLoginId = null;
		String name = null;

		if (LoginProviderType.KAKAO.equals(providerType)) {
			loginUserInfo = kakaoService.login(code);
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
			loginUser = createSocialUser(socialLoginId, name, provider);
			townUrl = townService.createTown(userRepository.save(loginUser));
		} else {
			loginUser = user.get();
		}

		String token = jwtTokenProvider.createAccessToken(String.valueOf(loginUser.getId()));
		System.out.println("jwt :" + token);
		System.out.println("townUrl : " + townUrl);

		return LoginResponse.builder()
			.jwt(token)
			.townUrl(townUrl)
			.build();
	}

	private User createSocialUser(String socialLoginId, String name, String provider) {
		User createUser = User.builder()
			.nickname(name)
			.socialLoginId(socialLoginId)
			.providerType(LoginProviderType.valueOf(provider.toUpperCase()))
			.build();
		return createUser;
	}
}
