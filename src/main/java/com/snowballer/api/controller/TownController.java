package com.snowballer.api.controller;

import com.snowballer.api.common.dto.ResponseDto;
import com.snowballer.api.dto.request.SubmitTownNameRequest;
import com.snowballer.api.dto.response.QuestionResponse;
import com.snowballer.api.dto.response.ResultResponse;
import com.snowballer.api.dto.response.TownResponse;
import com.snowballer.api.dto.response.TownSnowmanResponse;
import com.snowballer.api.service.QuestionService;
import com.snowballer.api.service.TownService;
import com.snowballer.api.service.TownSnowmanService;
import com.snowballer.api.service.UserService;
import javax.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snowballer.api.dto.request.SubmitAnswerRequest;
import com.snowballer.api.dto.request.SubmitLetterRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TownController {
	private final TownService townService;
	private final TownSnowmanService townSnowmanService;
	private final QuestionService questionService;

	// TODO 회원가입/로그인 API

	/**
	 * 유저 개인 마을 정보 조회하기 API
	 * @param townUrl 유저 마을 URL
	 * @return 유저의 마을 정보
	 */
	@GetMapping("/{townUrl}/town")
	public ResponseDto<TownResponse> retrieveTown(@PathVariable String townUrl) {
		return new ResponseDto(townService.getTown(townUrl));
	}

	/**
	 * 눈사람 상세 정보 조회 API
	 * @param snowmanId 유저 눈사람 ID
	 * @return 눈사람 상세 정보
	 */
	@GetMapping("/snowman/{snowmanId}")
	public ResponseDto<TownSnowmanResponse> retrieveSnowman(@PathVariable Long snowmanId) {
		return new ResponseDto(townSnowmanService.getLetter(snowmanId));
	}

	/**
	 * 특정 유저에 대한 질문 조회 API
	 * @param townUrl 유저 마을 URL
	 * @return 질문 List
	 */
	@GetMapping("/{townUrl}/question")
	public ResponseDto<QuestionResponse> retrieveQuestion(@PathVariable String townUrl) {
		System.out.println("/townUrl/Get");
		return new ResponseDto(questionService.getQuestion(townUrl));
	}

	/**
	 * 특정 유저에 대한 질문 답변 제출 API
	 * @param townUrl 유저 마을 URL
	 * @return 답변에 의해 만들어진 눈사람 정보
	 */
	@PostMapping("/{townUrl}/question")
	public ResponseDto<ResultResponse> submitAnswer(@PathVariable String townUrl, @RequestBody SubmitAnswerRequest request) {
		System.out.println("/townUrl/Post");
		return new ResponseDto(townSnowmanService.makeSnowman(townUrl, request));
	}

	/**
	 * 유저에게 편지 작성 API
	 * @param townUrl 유저 마을 URL
	 * @return success
	 */
	@PostMapping("/{townUrl}/letter")
	public ResponseDto<Null> submitLetter(@PathVariable String townUrl, @RequestBody SubmitLetterRequest request) {
		townSnowmanService.setLetter(townUrl, request);
		return new ResponseDto(null);
	}

	@PatchMapping("/{townUrl}/town/name")
	public ResponseDto<Null> modifyTown(@PathVariable String townUrl, @RequestBody SubmitTownNameRequest request) {
		townService.modifyTownName(townUrl, request);
		return new ResponseDto(null);
	}

}
