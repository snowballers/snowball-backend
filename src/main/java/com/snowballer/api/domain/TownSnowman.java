package com.snowballer.api.domain;

import com.snowballer.api.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class TownSnowman extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String letter;

    @Column(name = "sender_name")
    private String senderName;

    @Column(nullable = false)
    private Boolean seen;

    @Column(name = "have_letter")
    private Boolean haveLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snowman_id")
    private Snowman snowman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id")
    private Town town;

}
