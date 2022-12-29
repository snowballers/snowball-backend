package com.snowballer.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
	private String jwt;
	private String townUrl;
}
