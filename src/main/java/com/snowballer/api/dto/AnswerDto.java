package com.snowballer.api.dto;

import com.snowballer.api.domain.Answer;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class AnswerDto {

    private Long id;

    private String content;

    public static AnswerDto toResponse(Answer answer) {
        return AnswerDto.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .build();
    }
}
