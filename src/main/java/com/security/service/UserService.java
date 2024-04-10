package com.security.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.security.entity.User;
import com.security.exception.EmailAlreadyExistsException;
import com.security.exception.PasswordDontMatchException;
import com.security.exception.UserNotFoundException;


public interface UserService {

	public User findUserById(Long userId) throws UsernameNotFoundException;

	public boolean emailExists(String email) throws EmailAlreadyExistsException;

	public User findUserByEmail(String email) throws UsernameNotFoundException;

	public User saveUser(User user) throws UserNotFoundException;


	public User findVcodebyUser(String code)throws UserNotFoundException;

	public void updatePassword(String jwt, String password, String confirmPassword) throws PasswordDontMatchException;

}
