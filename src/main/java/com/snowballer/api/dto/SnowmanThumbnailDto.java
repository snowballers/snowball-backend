package com.snowballer.api.dto;

import com.snowballer.api.domain.TownSnowman;
import lombok.Builder;

@Builder
public class SnowmanThumbnailDto {

    private Long id;

    private String imageUrl;

    private Boolean seen;

    public static SnowmanThumbnailDto toResponse(TownSnowman townSnowman) {
        return SnowmanThumbnailDto.builder()
            .id(townSnowman.getId())
            .imageUrl(townSnowman.getSnowman().getImageUrl())
            .seen(townSnowman.getSeen())
            .build();
    }
}
