package com.snowballer.api.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
	private String provider;
	private String code;
}
