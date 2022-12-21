package com.snowballer.api.dto.response;

import com.snowballer.api.domain.Question;
import com.snowballer.api.domain.User;
import com.snowballer.api.dto.QuestionAnswerDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class QuestionResponse {

    private String name;

    private Integer totalQuestion;

    private List<QuestionAnswerDto> questions;

    public static QuestionResponse toResponse(User user, List<Question> questionList) {

        List<QuestionAnswerDto> questionAnswerDtoList = new ArrayList<QuestionAnswerDto>();
        for (Question question: questionList) {
            questionAnswerDtoList.add(QuestionAnswerDto.toResponse(question));
        }

        return QuestionResponse.builder()
            .name(user.getNickname())
            .totalQuestion(questionList.size())
            .questions(questionAnswerDtoList)
            .build();
    }
}
