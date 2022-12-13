package com.snowballer.api.common.exception;

import com.snowballer.api.common.enums.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
	private final ErrorCode errorCode;
}
