package com.snowballer.api.service;

import com.snowballer.api.domain.TownSnowman;
import com.snowballer.api.dto.request.SubmitLetterRequest;
import com.snowballer.api.dto.response.TownSnowmanResponse;
import com.snowballer.api.repository.TownSnowmanRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TownSnowmanService {

    private final UrlService urlService;
    private final TownSnowmanRepository townSnowmanRepository;

    public TownSnowmanResponse getLetter(Long id) {

        // 권한 확인 (본인 마을의 눈사람인지)

        // townSnowman에서 조회
        TownSnowman townSnowman = townSnowmanRepository.findById(id)
            .orElseThrow(()  -> new NoSuchElementException("Snowballers Error: 접근할 수 없는 눈사람입니다."));

        // "haveLetter == True" 확인
        townSnowman.checkHaveLetter();

        // 만약, seen == false일 시, true로 값 변경
        townSnowman.changeSeen();
        townSnowmanRepository.save(townSnowman);

        // dto 생성 및 반환
        return TownSnowmanResponse.toResponse(townSnowman);
    }

    public void setLetter(String url, SubmitLetterRequest submitLetterRequest) {

        // townSnowman 조회
        TownSnowman townSnowman = townSnowmanRepository.findById(submitLetterRequest.getSnowmanId())
            .orElseThrow(() -> new NoSuchElementException("Snowballers Error: 접근할 수 없는 눈사람입니다."));

        // url을 townId로 변환
        Long townId = urlService.decoding(url);

        // townSnowman 값 입력
        townSnowman.writeLetter(townId, submitLetterRequest);
        townSnowmanRepository.save(townSnowman);
    }
}