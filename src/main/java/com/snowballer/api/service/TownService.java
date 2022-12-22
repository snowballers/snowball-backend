package com.snowballer.api.service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.domain.User;
import com.snowballer.api.dto.request.SubmitTownNameRequest;
import com.snowballer.api.dto.response.TownResponse;
import com.snowballer.api.repository.TownRepository;
import com.snowballer.api.repository.TownSnowmanRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TownService {

    private final UrlService urlService;
    private final TownRepository townRepository;
    private final TownSnowmanRepository townSnowmanRepository;

    /**
     * 마을 정보 조회
     * @param url
     * @return '마을 정보' 조회 결과
     */
    public TownResponse getTown(String url) {

        // url로 town 조회
        Town town = changeUrlToTown(url);

        // 본인 마을인지 권한 확인
        boolean isMine = false;

        // 눈사람 조회
        List<TownSnowman> townSnowmanList = townSnowmanRepository.findAllByTownId(town.getId());

        // dto 생성 및 반환
        return TownResponse.toResponse(town, townSnowmanList, isMine);
    }

    /**
     * url에 맞는 town 결과 조회
     * @param url
     * @return url이 나타내는 town 객체
     */
    public Town changeUrlToTown(String url) {
        Long townId = urlService.decoding(url);
        Town town = townRepository.findById(townId)
            .orElseThrow(() -> new RestApiException(ErrorCode.INVALID_TOWN_LINK));

        return town;
    }

    /**
     * 마을 생성
     * @param user
     * @return 생성된 마을 url
     */
    @Transactional
    public String createTown(User user) {

        Town town = townRepository.save(Town.builder()
            .name(user.getNickname())
            .user(user)
            .build());

        return urlService.encoding(town.getId());
    }

    @Transactional
    public void modifyTownName(String url, SubmitTownNameRequest submitTownNameRequest) {

        // 권한 확인

        // url로 town 조회
        Town town = changeUrlToTown(url);

        // townName 수정
        town.modifyName(submitTownNameRequest.getTownName());
        townRepository.save(town);
    }
}