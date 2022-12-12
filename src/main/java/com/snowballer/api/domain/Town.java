package com.snowballer.api.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter @Builder
public class Town {
    @Id
    @GeneratedValue
    private Long id;

    private String url;

    private String name;

    @Column(name="create_at")
    private LocalDateTime createAt;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "town")
    private List<TownSnowman> townSnowmanList;
}
