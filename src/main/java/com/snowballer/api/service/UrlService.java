package com.snowballer.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.User;
import com.snowballer.api.domain.UserState;
import com.snowballer.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

	private final UserRepository userRepository;

	static final char[] BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

	public String encoding(Long id) {
		StringBuilder url = new StringBuilder();
		Long value = id;

		do {
			url.append(BASE62[(int)(value % BASE62.length)]);
			value /= BASE62.length;
		} while (value > 0);

		return url.toString();
	}

	public Long decoding(String url) {
		Long id = 0L;
		int power = 1;

		for (int i = 0; i < url.length(); i++) {
			int digit = new String(BASE62).indexOf(url.charAt(i));
			id += digit * power;
			power *= BASE62.length;
		}

		return id;
	}

	public String getTownUrl(Long userId) {
		User user = userRepository.findByIdAndState(userId, UserState.ACTIVE)
			.orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

		List<Town> townList = user.getTownList();
		if (townList.isEmpty()) {
			throw new RestApiException(ErrorCode.NOT_FOUND_TOWN);
		}

		return UriComponentsBuilder.fromUriString(
				"https://www.snowtown.team/"
					+ "town/"
					+ encoding(townList.get(0).getId())
			)
			.build().toUriString();
	}
}
