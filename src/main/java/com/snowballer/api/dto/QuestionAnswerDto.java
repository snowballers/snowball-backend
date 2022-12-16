package com.snowballer.api.dto;

import com.snowballer.api.domain.Answer;
import com.snowballer.api.domain.Question;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
public class QuestionAnswerDto {

    private Long id;

    private String content;

    private Integer totalAnswers;

    private List<AnswerDto> answers;

    public static QuestionAnswerDto toResponse(Question question) {

        List<AnswerDto> answerDtoList = new ArrayList<AnswerDto>();
        for (Answer answer: question.getAnswerList()) {
            answerDtoList.add(AnswerDto.toResponse(answer));
        }

        return QuestionAnswerDto.builder()
            .id(question.getId())
            .content(question.getContent())
            .totalAnswers(question.getAnswerList().size())
            .answers(answerDtoList)
            .build();
    }
}
