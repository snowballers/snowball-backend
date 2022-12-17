package com.snowballer.api.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.snowballer.api.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserService extends DefaultOAuth2UserService {
	// private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> originOauth = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = originOauth.loadUser(userRequest);
		Map<String, Object> userAttributes = oAuth2User.getAttributes();

		String provider = userRequest.getClientRegistration().getRegistrationId();

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
			.getUserInfoEndpoint().getUserNameAttributeName();
		Long socialLoginId = null;
		String name = null;
		if (provider.equals("google")) {
			// TODO 구글 attribute 확인 !
			socialLoginId = (Long)userAttributes.get("id");
			name = (String)userAttributes.get("login");
		} else if (provider.equals("kakao")) {
			Map<String, Object> kakaoAttributes = (Map<String, Object>)userAttributes.get("kakao_account");
			socialLoginId = (Long)kakaoAttributes.get("id");
			Map<String, Object> temp = null;
			temp = (Map)kakaoAttributes.get("properties");
			name = (String)temp.get("nickname");
		}

		// Optional<User> user = userRepository.findBySocialLoginId(socialLoginId);
		//
		// if (user.isEmpty()) {
		// 	// TODO userService 회원가입 로직 작성
		// 	User createUser = null;
		// 	return CustomUserDetails.create(createUser, oAuth2User.getAttributes());
		// }
		//
		User tempUser = null;
		return CustomUserDetails.create(tempUser, oAuth2User.getAttributes());
	}
}
