package com.snowballer.api.domain;

import com.snowballer.api.domain.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;


@Getter @Builder
public class Town extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "town")
    private List<TownSnowman> townSnowmanList;
}
