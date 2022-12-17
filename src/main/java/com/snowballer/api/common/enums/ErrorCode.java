package com.snowballer.api.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	DEFAULT_ERROR_CODE(5000, HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다."),
	UNAUTHORIZED_REQUEST(4000, HttpStatus.UNAUTHORIZED, "허용되지 않는 권한입니다.");

	private final int code;
	private HttpStatus httpStatus;
	private String message;

	ErrorCode(int code, HttpStatus httpStatus, String message) {
		this.code = code;
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}
