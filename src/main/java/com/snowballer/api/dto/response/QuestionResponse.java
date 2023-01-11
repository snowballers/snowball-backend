package com.snowballer.api.dto.response;

import com.snowballer.api.domain.Question;
import com.snowballer.api.domain.Town;
import com.snowballer.api.dto.QuestionAnswerDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class QuestionResponse {

    private Integer totalQuestion;

    private List<QuestionAnswerDto> questions;

    public static QuestionResponse toResponse(Town town, List<Question> questionList) {

        List<QuestionAnswerDto> questionAnswerDtoList = new ArrayList<QuestionAnswerDto>();
        for (Question question: questionList) {
            questionAnswerDtoList.add(QuestionAnswerDto.toResponse(question, town.getName()));
        }

        return QuestionResponse.builder()
            .totalQuestion(questionList.size())
            .questions(questionAnswerDtoList)
            .build();
    }
}
