package com.snowballer.api.repository;

import com.snowballer.api.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository  extends JpaRepository<Question, Long> {

}
