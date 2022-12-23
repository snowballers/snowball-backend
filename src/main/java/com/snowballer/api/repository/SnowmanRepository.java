package com.snowballer.api.repository;

import com.snowballer.api.domain.Snowman;
import com.snowballer.api.domain.SnowmanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnowmanRepository extends JpaRepository<Snowman, Long> {
    Snowman findByType(SnowmanType type);
}
