package com.snowballer.api.domain;

import com.snowballer.api.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter @Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(name = "social_login_id")
    private Long socialLoginId;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private LoginProviderType providerType;

    @OneToMany(mappedBy = "user")
    List<Town> townList;
}
