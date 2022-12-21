package com.snowballer.api.dto.response;

import com.snowballer.api.domain.Snowman;
import com.snowballer.api.domain.User;
import com.snowballer.api.dto.SnowmanResultDto;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ResultResponse {

    private String nickname;

    private Integer percent;

    private SnowmanResultDto snowman;

    public static ResultResponse toResponse(User user, Integer percent, Snowman snowman) {
        return ResultResponse.builder()
            .nickname(user.getNickname())
            .percent(percent)
            .snowman(SnowmanResultDto.toResponse(snowman))
            .build();
    }
}
