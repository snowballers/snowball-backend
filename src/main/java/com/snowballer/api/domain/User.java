package com.snowballer.api.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String nickname;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "social_login_id")
    private String socialLoginId;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private LoginProviderType providerType;

    @OneToMany(mappedBy = "user")
    List<Town> townList;
}
