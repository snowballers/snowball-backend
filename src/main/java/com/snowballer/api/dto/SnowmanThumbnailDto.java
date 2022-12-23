package com.snowballer.api.dto;

import com.snowballer.api.domain.TownSnowman;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class SnowmanThumbnailDto {

    private Long id;

    private String type;

    private Boolean seen;

    public static SnowmanThumbnailDto toResponse(TownSnowman townSnowman) {
        return SnowmanThumbnailDto.builder()
            .id(townSnowman.getId())
            .type(townSnowman.getSnowman().getType().toString())
            .seen(townSnowman.getSeen())
            .build();
    }
}
