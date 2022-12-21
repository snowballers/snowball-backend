package com.snowballer.api.repository;

import com.snowballer.api.domain.TownSnowman;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownSnowmanRepository extends JpaRepository<TownSnowman, Long> {

    List<TownSnowman> findAllByTownId(Long townId);

    Integer countByTownIdAndHaveLetter(Long townId, Boolean haveLetter);

    Integer countByTownIdAndSnowmanIdAndHaveLetter(Long townId, Long snowmanId, Boolean haveLetter);
}
