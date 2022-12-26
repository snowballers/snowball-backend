package com.snowballer.api.dto.response;

import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.SnowmanDetailDto;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class TownSnowmanResponse {

    private String senderName;

    private Integer percent;

    private String letter;

    private SnowmanDetailDto snowman;

    public static TownSnowmanResponse toResponse(TownSnowman townSnowman, Integer percent) {
        return TownSnowmanResponse.builder()
            .senderName(townSnowman.getSenderName())
            .percent(percent)
            .letter(townSnowman.getLetter())
            .snowman(SnowmanDetailDto.toResponse(townSnowman))
            .build();
    }
}
