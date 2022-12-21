package com.snowballer.api.service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.response.TownResponse;
import com.snowballer.api.repository.TownRepository;
import com.snowballer.api.repository.TownSnowmanRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TownService {

    private final UrlService urlService;
    private final TownRepository townRepository;
    private final TownSnowmanRepository townSnowmanRepository;

    /**
     * 마을 정보 조회
     * @param url
     * @return 마을 정보 조회 결과
     */
    public TownResponse getTown(String url) {

        // url로 town 조회
        Town town = changeUrlToTown(url);

        // 본인 마을인지 권한 확인
        boolean isMine = false;

        // 눈사람 조회(where haveLetter = True)
        List<TownSnowman> townSnowmanList = townSnowmanRepository.findAllByTownIdAndHaveLetter(
            town.getId(), true);

        // dto 생성 및 반환
        return TownResponse.toResponse(town, townSnowmanList, isMine);
    }

    public Town changeUrlToTown(String url) {
        Long townId = urlService.decoding(url);
        Town town = townRepository.findById(townId)
            .orElseThrow(() -> new RestApiException(ErrorCode.INVALID_TOWN_LINK));

        return town;
    }
}
