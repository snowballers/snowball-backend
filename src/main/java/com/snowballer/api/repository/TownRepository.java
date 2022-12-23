package com.snowballer.api.repository;

import java.util.List;

import com.snowballer.api.domain.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Long> {

}
