package com.snowballer.api.dto;

import com.snowballer.api.domain.Snowman;
import lombok.Builder;

@Builder
public class SnowmanResultDto {

    private Long id;

    private String name;

    private String imageUrl;

    private String description;

    public static SnowmanResultDto toResponse(Snowman snowman) {
        return SnowmanResultDto.builder()
            .id(snowman.getId())
            .name(snowman.getName())
            .imageUrl(snowman.getImageUrl())
            .description(snowman.getDescription())
            .build();
    }
}
