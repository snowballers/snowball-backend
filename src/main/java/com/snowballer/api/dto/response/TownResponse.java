package com.snowballer.api.dto.response;

import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.SnowmanThumbnailDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
public class TownResponse {

    private Boolean isMind;

    private String nickname;

    private String townName;

    private Integer totalSnowman;

    private List<SnowmanThumbnailDto> snowmans;

    public static TownResponse toResponse(Town town, List<TownSnowman> townSnowmanList, Boolean isMind) {
        // TownSnowman
        List<SnowmanThumbnailDto> snowmanThumbnailDtoList = new ArrayList<SnowmanThumbnailDto>();
        for (TownSnowman townSnowman: townSnowmanList) {
            snowmanThumbnailDtoList.add(SnowmanThumbnailDto.toResponse(townSnowman));
        }

        return TownResponse.builder()
            .isMind(isMind)
            .nickname(town.getUser().getNickname())
            .townName(town.getName())
            .totalSnowman(townSnowmanList.size())
            .snowmans(snowmanThumbnailDtoList)
            .build();
    }
}
