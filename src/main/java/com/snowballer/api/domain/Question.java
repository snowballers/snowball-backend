package com.snowballer.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Question {
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "question")
    private List<Answer> answerList = new ArrayList<>();
}
