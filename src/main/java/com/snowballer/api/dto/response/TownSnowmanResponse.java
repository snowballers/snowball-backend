package com.snowballer.api.dto.response;

import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.SnowmanDetailDto;
import lombok.Builder;

@Builder
public class TownSnowmanResponse {

    private String senderName;

    private String letter;

    private SnowmanDetailDto snowmanDetailDto;

    public static TownSnowmanResponse toResponse(TownSnowman townSnowman) {
        return TownSnowmanResponse.builder()
            .senderName(townSnowman.getSenderName())
            .letter(townSnowman.getLetter())
            .snowmanDetailDto(SnowmanDetailDto.toResponse(townSnowman))
            .build();
    }
}
