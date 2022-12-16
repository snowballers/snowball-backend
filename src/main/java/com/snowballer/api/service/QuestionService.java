package com.snowballer.api.service;

import com.snowballer.api.domain.Question;
import com.snowballer.api.domain.Town;
import com.snowballer.api.dto.response.QuestionResponse;
import com.snowballer.api.repository.QuestionRepository;
import com.snowballer.api.repository.TownRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final UrlService urlService;
    private final QuestionRepository questionRepository;
    private final TownRepository townRepository;

    public QuestionResponse getQuestion(String url) {

        // url로 user 조회
        Long townId = urlService.decoding(url);
        Town town = townRepository.findById(townId)
            .orElseThrow(()  -> new NoSuchElementException("Snowballers Error: 접근할 수 없는 링크입니다."));

        // question 조회
        List<Question> questionList = questionRepository.findAll();

        // dto로 반환
        return QuestionResponse.toResponse(town.getUser(), questionList);
    }
}
