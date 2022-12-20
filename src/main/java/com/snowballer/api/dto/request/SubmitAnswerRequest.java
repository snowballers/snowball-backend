package com.snowballer.api.dto.request;

import java.util.List;

import com.snowballer.api.dto.QuestionDto;

import lombok.Getter;

@Getter
public class SubmitAnswerRequest {
	private int totalQuestion;
	private List<QuestionDto> questions;
}
