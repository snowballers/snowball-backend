package com.snowballer.api.service;

import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.domain.Snowman;
import com.snowballer.api.domain.SnowmanType;
import com.snowballer.api.domain.Town;
import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.domain.User;
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
    private final UserService userService;

    /**
     * 눈사람 및 편지 조회
     * @param id
     * @return 눈사람 결과 조회
     */
    @Transactional
    public TownSnowmanResponse getLetter(Long id) {

        // townSnowman에서 조회
        TownSnowman townSnowman = townSnowmanRepository.findById(id)
            .orElseThrow(()  -> new RestApiException(ErrorCode.NOT_FOUND_SNOWMAN));

        // town의 user 유효성 체크
        townSnowman.getTown().getUser().checkUserState();

        // 권한 확인 (본인 마을의 눈사람인지)
        userService.checkAuthorized(townSnowman.getTown().getUser());

        // 만약, seen == false일 시, true로 값 변경
        townSnowman.changeSeen();
        townSnowmanRepository.save(townSnowman);

        // town_snowman에 같은 눈사람이 몇개인지 percent 계산
        Integer percent = calculatePercent(townSnowman.getTown().getId(), townSnowman.getSnowman().getId());

        // dto 생성 및 반환
        return TownSnowmanResponse.toResponse(townSnowman, percent);
    }

    /**
     * 편지 등록
     * @param url
     * @param submitLetterRequest
     */
    @Transactional
    public void setLetter(String url, SubmitLetterRequest submitLetterRequest) {

        // townSnowman 조회
        TownSnowman townSnowman = townSnowmanRepository.findById(submitLetterRequest.getSnowmanId())
            .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_SNOWMAN));

        // town의 user 유효성 체크
        townSnowman.getTown().getUser().checkUserState();

        // url을 townId로 변환
        Long townId = urlService.decoding(url);

        // townSnowman 값 입력
        townSnowman.writeLetter(townId, submitLetterRequest);
        townSnowmanRepository.save(townSnowman);
    }

    /**
     * 질문에 대한 답변으로 눈사람 만들기
     * @param url
     * @param submitAnswerRequest
     * @return 답변 결과 반환
     */
    @Transactional
    public ResultResponse makeSnowman(String url, SubmitAnswerRequest submitAnswerRequest) {

        // url로 town 조회
        Town town = townService.changeUrlToTown(url);

        // 응답으로 MBTI 결과 계산
        Snowman snowman = answerService.analysisType(submitAnswerRequest);

        // town-snowman에 정보 저장 ( letter = Null, seen = False )
        TownSnowman townSnowman = TownSnowman.buildSnowman(snowman, town, submitAnswerRequest.getSender());
        townSnowmanRepository.save(townSnowman);

        // town_snowman에 같은 눈사람이 몇개인지 percent 계산
        Integer percent = calculatePercent(town.getId(), snowman.getId());

        // dto 생성 및 반환
        return ResultResponse.toResponse(town.getUser(), percent, townSnowman);
    }

    /**
     * percent 계산
     * @param townId
     * @param snowmanId
     * @return percent
     */
    private Integer calculatePercent(Long townId, Long snowmanId) {

        Integer totalSize = townSnowmanRepository.countByTownId(townId);
        Integer typeSize = townSnowmanRepository.countByTownIdAndSnowmanId(townId, snowmanId);

        return (int) Math.round((double)(typeSize) / (double)(totalSize) * 100);
    }
}