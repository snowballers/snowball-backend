package com.snowballer.api.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
public class Snowman {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnowmanType type;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String state;

    @OneToMany(mappedBy = "snowman")
    private List<TownSnowman> townSnowmanList;
}
