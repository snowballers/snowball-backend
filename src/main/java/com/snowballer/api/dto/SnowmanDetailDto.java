package com.snowballer.api.dto;

import com.snowballer.api.domain.TownSnowman;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class SnowmanDetailDto {

    private String name;

    private String imageURL;

    private String description;

    public static SnowmanDetailDto toResponse(TownSnowman townSnowman) {
        return SnowmanDetailDto.builder()
            .name(townSnowman.getSnowman().getName())
            .imageURL(townSnowman.getSnowman().getImageUrl())
            .description(townSnowman.getSnowman().getDescription())
            .build();
    }
}
