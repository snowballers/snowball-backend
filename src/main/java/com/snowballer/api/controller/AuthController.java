package com.snowballer.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snowballer.api.common.dto.ResponseDto;
import com.snowballer.api.dto.request.LoginRequest;
import com.snowballer.api.dto.response.LoginResponse;
import com.snowballer.api.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/login")
	public ResponseDto<LoginResponse> getAccessToken(@RequestBody LoginRequest request) {
		return new ResponseDto(authService.login(request));
	}
}
