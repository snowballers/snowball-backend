package com.snowballer.api.common.enums;

public enum ErrorCode {
	UNAUTHORIZED_REQUEST(4000, "허용되지 않는 권한입니다.");

	private int code;
	private String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
