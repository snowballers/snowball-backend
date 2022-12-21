package com.snowballer.api.dto.response;

import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.SnowmanThumbnailDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class TownResponse {

    private Boolean isMine;

    private String nickname;

    private String townName;

    private Integer totalSnowman;

    private List<SnowmanThumbnailDto> snowmans;

    public static TownResponse toResponse(Town town, List<TownSnowman> townSnowmanList, Boolean isMine) {
        // TownSnowman
        List<SnowmanThumbnailDto> snowmanThumbnailDtoList = new ArrayList<SnowmanThumbnailDto>();
        for (TownSnowman townSnowman: townSnowmanList) {
            snowmanThumbnailDtoList.add(SnowmanThumbnailDto.toResponse(townSnowman));
        }

        return TownResponse.builder()
            .isMine(isMine)
            .nickname(town.getUser().getNickname())
            .townName(town.getName())
            .totalSnowman(townSnowmanList.size())
            .snowmans(snowmanThumbnailDtoList)
            .build();
    }
}
