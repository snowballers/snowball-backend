package com.snowballer.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TownController {

	// TODO 회원가입/로그인 API

	/**
	 * 유저 개인 마을 정보 조회하기 API
	 * @param townUrl 유저 마을 URL
	 * @return 유저의 마을 정보
	 */
	@GetMapping("/{townUrl}/town")
	public ResponseEntity<String> retrieveTown(@PathVariable String townUrl) {
		return ResponseEntity.ok("town");
	}

	/**
	 * 눈사람 상세 정보 조회 API
	 * @param snowmanId 유저 눈사람 ID
	 * @return 눈사람 상세 정보
	 */
	@GetMapping("/snowman/{snowmanId}")
	public ResponseEntity<String> retrieveSnowman(@PathVariable Long snowmanId) {
		return ResponseEntity.ok("snowman");
	}

	/**
	 * 특정 유저에 대한 질문 조회 API
	 * @param townUrl 유저 마을 URL
	 * @return 질문 List
	 */
	@GetMapping("/{townUrl}/question")
	public ResponseEntity<String> retrieveQuestion(@PathVariable String townUrl) {
		return ResponseEntity.ok("question");
	}

	/**
	 * 특정 유저에 대한 질문 답변 제출 API
	 * @param townUrl 유저 마을 URL
	 * @return 답변에 의해 만들어진 눈사람 정보
	 */
	@PostMapping("/{townUrl}/question")
	public ResponseEntity<String> submitAnswer(@PathVariable String townUrl) {
		return ResponseEntity.ok("made-snowman");
	}

	/**
	 * 유저에게 편지 작성 API
	 * @param townUrl 유저 마을 URL
	 * @return success
	 */
	@PostMapping("/{townUrl}/letter")
	public ResponseEntity<String> submitLetter(@PathVariable String townUrl) {
		return ResponseEntity.ok("letter success");
	}
}
