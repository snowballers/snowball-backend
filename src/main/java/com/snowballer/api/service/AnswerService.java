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

    public Snowman analysisType(SubmitAnswerRequest submitAnswerRequest) {

        // 응답 결과 계산
        Map<String, Integer> type = calculateType(submitAnswerRequest);

        // Type 만들기
        String mbti = makeType(type);
        System.out.println(mbti);

        // type에 맞는 snowman 조회
        Snowman snowman = snowmanRepository.findByType(SnowmanType.valueOf(mbti));

        return snowman;
    }

    private String makeType(Map<String, Integer> type) {
        String mbti = "";
        mbti += type.get("E") > type.get("I") ? "E" : "I";
        mbti += type.get("N") > type.get("S") ? "N" : "S";
        mbti += type.get("F") > type.get("T") ? "F" : "T";
        mbti += type.get("J") > type.get("P") ? "J" : "P";

        return mbti;
    }

    private Map calculateType(SubmitAnswerRequest submitAnswerRequest) {
        Map<String, Integer> type = setType();

        for (int i=0; i< submitAnswerRequest.getTotalQuestion(); i++) {
            Long questionId = submitAnswerRequest.getQuestions().get(i).getId();
            Long answerId = submitAnswerRequest.getQuestions().get(i).getAnswerId();

            Answer answer = answerRepository.findByIdAndQuestionId(answerId, questionId);
            int count = type.containsKey(answer.getAnswerType()) ? type.get(answer.getAnswerType()) : 0;
            type.put(answer.getAnswerType().name(), count + 1);
        }

        return type;
    }

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
