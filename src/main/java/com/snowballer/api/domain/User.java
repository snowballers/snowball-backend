package com.snowballer.api.domain;

import com.snowballer.api.common.domain.BaseTimeEntity;
import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(name = "social_login_id")
    private String socialLoginId;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private LoginProviderType providerType;

    @OneToMany(mappedBy = "user")
    List<Town> townList = new ArrayList<>();

    @Builder
    public User(String nickname, String socialLoginId, LoginProviderType providerType) {
        this.nickname = nickname;
        this.socialLoginId = socialLoginId;
        this.providerType = providerType;
        this.state = UserState.ACTIVE;
    }

    public void changeStateOff() {
        this.state = UserState.DELETED;
    }

    public void checkUserState() {
        if (this.state.equals(UserState.DELETED)) {
            throw new RestApiException(ErrorCode.DELETED_USER_TOWN);
        }
    }
}
