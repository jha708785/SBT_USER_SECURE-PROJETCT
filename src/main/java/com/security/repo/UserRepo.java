package com.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.entity.User;

public interface UserRepo  extends JpaRepository<User, Long>{


    boolean existsByEmail(String email);
	Optional<User> findById(Long userId);
	public User  findByEmail(String email);
	public User findByVcode(String code);

}
