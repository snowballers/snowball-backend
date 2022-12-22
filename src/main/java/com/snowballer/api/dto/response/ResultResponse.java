package com.snowballer.api.dto.response;

import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.domain.User;
import com.snowballer.api.dto.SnowmanDetailDto;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ResultResponse {

    private String nickname;

    private Integer percent;

    private SnowmanDetailDto snowman;

    public static ResultResponse toResponse(User user, Integer percent, TownSnowman townSnowman) {
        return ResultResponse.builder()
            .nickname(user.getNickname())
            .percent(percent)
            .snowman(SnowmanDetailDto.toResponse(townSnowman))
            .build();
    }
}
