package com.snowballer.api.common.dto;

public class DataResponse<T> extends BaseResponse {
	private T data;

	public DataResponse(T data) {
		super(true);
		this.data = data;
	}
}
