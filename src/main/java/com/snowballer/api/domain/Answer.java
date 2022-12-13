package com.snowballer.api.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
public class Answer {
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
