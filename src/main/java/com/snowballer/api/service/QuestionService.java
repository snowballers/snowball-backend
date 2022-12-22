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

    private final QuestionRepository questionRepository;
    private final TownService townService;

    /**
     * 질문 조회하기
     * @param url
     * @return DB의 모든 '질문 및 응답' 조회 결과
     */
    public QuestionResponse getQuestion(String url) {

        // url로 town 조회
        Town town = townService.changeUrlToTown(url);

        // question 조회
        List<Question> questionList = questionRepository.findAll();

        // dto로 반환
        return QuestionResponse.toResponse(town.getUser(), questionList);
    }
}
