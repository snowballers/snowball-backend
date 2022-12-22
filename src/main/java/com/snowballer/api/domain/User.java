package com.snowballer.api.domain;

import com.snowballer.api.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
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
    List<Town> townList = new ArrayList<>();
}
