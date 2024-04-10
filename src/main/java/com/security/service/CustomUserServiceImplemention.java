package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.entity.User;
import com.security.repo.UserRepo;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;

@Service
public class CustomUserServiceImplemention implements UserDetailsService {
	@Autowired
	private UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = repo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("user Not Found ");
		}

		List<GrantedAuthority> authoritie = new ArrayList<>();

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authoritie);
	}

}
