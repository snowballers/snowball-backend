package com.snowballer.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snowballer.api.domain.User;
import com.snowballer.api.domain.UserState;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findBySocialLoginIdAndState(String socialLoginId, UserState userState);

	Optional<User> findByIdAndState(Long id, UserState userState);
}
