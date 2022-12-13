package com.snowballer.api.common.dto;

import com.snowballer.api.common.enums.ErrorCode;

public class ErrorResponse extends BaseResponse {
	private int code;
	private String message;

	public ErrorResponse(ErrorCode code) {
		super(false);
		this.code = code.getCode();
		this.message = code.getMessage();
	}
}
