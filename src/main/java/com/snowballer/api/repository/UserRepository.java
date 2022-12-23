package com.snowballer.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snowballer.api.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findBySocialLoginId(String socialLoginId);
}
