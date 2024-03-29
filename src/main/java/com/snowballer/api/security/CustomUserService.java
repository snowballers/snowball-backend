package com.snowballer.api.security;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.snowballer.api.domain.LoginProviderType;
import com.snowballer.api.domain.User;
import com.snowballer.api.domain.UserState;
import com.snowballer.api.repository.UserRepository;
import com.snowballer.api.service.TownService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final TownService townService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> originOauth = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = originOauth.loadUser(userRequest);
		Map<String, Object> userAttributes = oAuth2User.getAttributes();

		String provider = userRequest.getClientRegistration().getRegistrationId();

		String socialLoginId = null;
		String name = null;
		
		if (provider.equals("google")) {
			socialLoginId = (String) userAttributes.get("sub");
			name = (String)userAttributes.get("name");
		} else if (provider.equals("kakao")) {
			socialLoginId = userAttributes.get("id").toString();
			Map<String, Object> temp = null;
			temp = (Map)userAttributes.get("properties");
			name = (String)temp.get("nickname");
		}

		Optional<User> user = userRepository.findBySocialLoginIdAndState(socialLoginId, UserState.ACTIVE);

		 if (user.isEmpty()) {
			 User createUser = createSocialUser(socialLoginId, name, provider);
			 return CustomUserDetails.create(createUser, oAuth2User.getAttributes());
		 }

		return CustomUserDetails.create(user.get(), oAuth2User.getAttributes());
	}

	private User createSocialUser(String socialLoginId, String name, String provider) {
		User createUser = User.builder()
			.nickname(name)
			.socialLoginId(socialLoginId)
			.providerType(LoginProviderType.valueOf(provider.toUpperCase()))
			.build();
		townService.createTown(userRepository.save(createUser));
		return createUser;
	}
}
