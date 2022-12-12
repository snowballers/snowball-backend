package com.snowballer.api.domain;

import lombok.Getter;

import javax.persistence.*;

@SequenceGenerator(
        name = "TOWN_SNOWMAN_SEQ_GENERATOR",
        sequenceName = "TOWN_SNOWMAN_SEQ", // 시퀸스 명
        initialValue = 10000000, // 초기 값
        allocationSize = 1 // 미리 할당 받을 시퀀스 수
)
@Getter
public class TownSnowman {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOWN_SNOWMAN_SEQ_GENERATOR")
    private Long id;

    private String letter;

    @Column(name = "sender_name")
    private String senderName;

    @Column(nullable = false)
    private Boolean seen;

    @ManyToOne
    private Snowman snowman;

    @ManyToOne
    private Town town;

}
