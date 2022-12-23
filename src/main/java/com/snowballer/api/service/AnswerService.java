package com.snowballer.api.service;

import com.snowballer.api.domain.Answer;
import com.snowballer.api.domain.Snowman;
import com.snowballer.api.domain.SnowmanType;
import com.snowballer.api.dto.request.SubmitAnswerRequest;
import com.snowballer.api.repository.AnswerRepository;
import com.snowballer.api.repository.SnowmanRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final SnowmanRepository snowmanRepository;

    /**
     * 타입 분석해 적합한 눈사람 반환
     * @param submitAnswerRequest
     * @return 결과 눈사람 반환
     */
    public Snowman analysisType(SubmitAnswerRequest submitAnswerRequest) {

        // 응답 결과 계산
        Map<String, Integer> types = calculateType(submitAnswerRequest);

        // Type 만들기
        String type = makeType(types);

        // type에 맞는 snowman 조회
        Snowman snowman = snowmanRepository.findByType(SnowmanType.valueOf(type));

        return snowman;
    }

    /**
     * 응답 결과로 type 만들기
     * @param types
     * @return type 결과 반환
     */
    private String makeType(Map<String, Integer> types) {
        String type = "";
        type += types.get("E") > types.get("I") ? "E" : "I";
        type += types.get("N") > types.get("S") ? "N" : "S";
        type += types.get("F") > types.get("T") ? "F" : "T";
        type += types.get("J") > types.get("P") ? "J" : "P";

        return type;
    }

    /**
     * 응답 결과 계산
     * @param submitAnswerRequest
     * @return 결과 계산한 Map
     */
    private Map calculateType(SubmitAnswerRequest submitAnswerRequest) {
        // 초기 Map
        Map<String, Integer> type = setType();

        // 응답 결과 계산
        for (int i=0; i< submitAnswerRequest.getTotalQuestion(); i++) {
            Long questionId = submitAnswerRequest.getQuestions().get(i).getId();
            Long answerId = submitAnswerRequest.getQuestions().get(i).getAnswerId();

            // answer의 AnswerType 조회
            Answer answer = answerRepository.findByIdAndQuestionId(answerId, questionId);

            // type에 AnswerType 추가
            type.put(answer.getAnswerType().name(), type.get(answer.getAnswerType().name()) + 1);
        }

        return type;
    }

    /**
     * 응답 결과 셀 Map 세팅
     * @return 초기 Map
     */
    private Map setType() {
        Map<String, Integer> type = new HashMap<>();
        type.put("E", 0);
        type.put("I", 0);
        type.put("N", 0);
        type.put("S", 0);
        type.put("F", 0);
        type.put("T", 0);
        type.put("J", 0);
        type.put("P", 0);

        return type;
    }
}
