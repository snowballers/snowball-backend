package com.snowballer.api.service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.Snowman;
import com.snowballer.api.domain.SnowmanType;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.request.SubmitAnswerRequest;
import com.snowballer.api.dto.request.SubmitLetterRequest;
import com.snowballer.api.dto.response.ResultResponse;
import com.snowballer.api.dto.response.TownSnowmanResponse;
import com.snowballer.api.repository.SnowmanRepository;
import com.snowballer.api.repository.TownRepository;
import com.snowballer.api.repository.TownSnowmanRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TownSnowmanService {

    private final UrlService urlService;
    private final TownService townService;
    private final TownSnowmanRepository townSnowmanRepository;
    private final AnswerService answerService;

    @Transactional
    public TownSnowmanResponse getLetter(Long id) {

        // 권한 확인 (본인 마을의 눈사람인지)

        // townSnowman에서 조회
        TownSnowman townSnowman = townSnowmanRepository.findById(id)
            .orElseThrow(()  -> new RestApiException(ErrorCode.NOT_FOUNT_SNOWMAN));

        // "haveLetter == True" 확인
        townSnowman.checkHaveLetter();

        // 만약, seen == false일 시, true로 값 변경
        townSnowman.changeSeen();
        townSnowmanRepository.save(townSnowman);

        // dto 생성 및 반환
        return TownSnowmanResponse.toResponse(townSnowman);
    }

    @Transactional
    public void setLetter(String url, SubmitLetterRequest submitLetterRequest) {

        // townSnowman 조회
        TownSnowman townSnowman = townSnowmanRepository.findById(submitLetterRequest.getSnowmanId())
            .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUNT_SNOWMAN));

        // url을 townId로 변환
        Long townId = urlService.decoding(url);

        // townSnowman 값 입력
        townSnowman.writeLetter(townId, submitLetterRequest);
        townSnowmanRepository.save(townSnowman);
    }

    @Transactional
    public ResultResponse makeSnowman(String url, SubmitAnswerRequest submitAnswerRequest) {

        // url을 townId로 변환
        Town town = townService.changeUrlToTown(url);

        // 응답으로 MBTI 결과 계산
        Snowman snowman = answerService.analysisType(submitAnswerRequest);

        // town-snowman에 정보 저장 ( letter = Null, senderName = Null, seen = False, haveLetter = False )
        TownSnowman townSnowman = TownSnowman.buildSnowman(snowman, town, submitAnswerRequest.getSender());
        townSnowmanRepository.save(townSnowman);

        // town_snowman에 같은 눈사람이 몇개인지 percent 계산
        Integer percent = calculatePercent(town.getId(), snowman.getId());

        // dto 생성 및 반환
        return ResultResponse.toResponse(town.getUser(), percent, townSnowman);
    }

    private Integer calculatePercent(Long townId, Long snowmanId) {
        Integer totalSize = townSnowmanRepository.countByTownIdAndHaveLetter(townId, true);
        Integer typeSize = townSnowmanRepository.countByTownIdAndSnowmanIdAndHaveLetter(townId, snowmanId, true);

        return (int) Math.round((double)(typeSize + 1) / (double)(totalSize + 1) * 100);
    }
}