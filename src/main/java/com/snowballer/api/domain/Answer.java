package com.snowballer.api.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
public class Answer {
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private Question question;
}
