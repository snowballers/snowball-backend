package com.snowballer.api.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.snowballer.api.common.dto.ErrorResponse;
import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(RestApiException e) {
		return makeErrorResponse(e.getErrorCode());
	}

	private ResponseEntity<ErrorResponse> makeErrorResponse(final ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.builder()
				.code(errorCode)
				.build());
	}
}
