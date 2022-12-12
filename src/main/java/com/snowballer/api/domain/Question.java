package com.snowballer.api.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
public class Question {
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "question")
    private List<Answer> answerList;
}
