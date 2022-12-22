package com.snowballer.api.repository;

import com.snowballer.api.domain.TownSnowman;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownSnowmanRepository extends JpaRepository<TownSnowman, Long> {

    List<TownSnowman> findAllByTownId(Long townId);

    Integer countByTownId(Long townId);

    Integer countByTownIdAndSnowmanId(Long townId, Long snowmanId);
}
