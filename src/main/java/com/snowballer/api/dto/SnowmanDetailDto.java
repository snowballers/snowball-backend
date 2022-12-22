package com.snowballer.api.dto;

import com.snowballer.api.domain.TownSnowman;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class SnowmanDetailDto {

    private Long id;

    private String name;

    private String type;

    private String description;

    public static SnowmanDetailDto toResponse(TownSnowman townSnowman) {
        return SnowmanDetailDto.builder()
            .id(townSnowman.getId())
            .name(townSnowman.getSnowman().getName())
            .type(townSnowman.getSnowman().getType().name())
            .description(townSnowman.getSnowman().getDescription())
            .build();
    }
}
