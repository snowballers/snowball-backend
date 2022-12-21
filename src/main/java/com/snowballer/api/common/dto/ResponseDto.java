package com.snowballer.api.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ResponseDto<T> {
	private final Boolean success;
	private final T data;
	private ExceptionDto error;

	public ResponseDto(@Nullable T data) {
		this.success = true;
		this.data = data;
		this.error = null;
	}

	// 사용자 정의 Exception 에 따른 ExceptionDto 리턴
	public static ResponseEntity<Object> toResponseEntity(RestApiException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(ResponseDto.builder()
				.success(false)
				.data(null)
				.error(new ExceptionDto(e.getErrorCode())));
	}

	// 그 외 Exception 에 따른 ExceptionDto 리턴
	public static ResponseEntity<Object> toResponseEntity(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ResponseDto.builder()
				.success(false)
				.data(null)
				.error(new ExceptionDto(ErrorCode.DEFAULT_ERROR_CODE)));
	}
}
