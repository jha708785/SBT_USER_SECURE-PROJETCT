package com.security.service;

import java.util.Optional;
import java.util.UUID;

import com.security.jwt.JwtProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.entity.User;
import com.security.exception.EmailAlreadyExistsException;
import com.security.exception.PasswordDontMatchException;
import com.security.exception.UserNotFoundException;
import com.security.repo.UserRepo;
import com.security.request.RegesterRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private JwtProviders pro;

	public UserServiceImpl(UserRepo repo) {
		this.repo = repo;
	}

	@Override
	public User findUserById(Long userId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		Optional<User> findById = repo.findById(userId);

		if (findById.isEmpty()) {
			throw new UsernameNotFoundException("user id not found" + userId);
		}
		return findById.get();

	}

	@Override
	public boolean emailExists(String email) throws EmailAlreadyExistsException {
		// TODO Auto-generated method stub
		return repo.existsByEmail(email);
	}

	@Override
	public User findUserByEmail(String email) throws UsernameNotFoundException {

		User useremail = repo.findByEmail(email);
		if (useremail == null) {
			throw new UsernameNotFoundException("user name not found");
		}
		return useremail;
	}

	@Override
	public User saveUser(User user) throws UserNotFoundException {

		String email = user.getEmail();
		if (emailExists(email)) {
			throw new EmailAlreadyExistsException();
		} else {
			user.setEmail(user.getEmail());
			user.setPassword(encoder.encode(user.getPassword()));
			user.setRole("User_Role");
			user.setVcode(UUID.randomUUID().toString());

			repo.save(user);
		}

		return user;
	}

	@Override
	public User findVcodebyUser(String code) throws UserNotFoundException {

		User user = repo.findByVcode(code);
		if(user==null){
			throw  new UserNotFoundException("userr not found");
		}
		 return user;
	}

	@Override
	public void updatePassword(String jwt, String password, String confirmPassword)
			throws PasswordDontMatchException {


		String email = JwtProviders.getEmail(jwt);
		User user = findUserByEmail(email);

		if (emailExists(user.getEmail())) {

			if (password.equals(confirmPassword)) {
				user.setPassword(confirmPassword);

				repo.save(user);
			} else { // if the password doesn't match, throw an exception
				throw new PasswordDontMatchException();
			}
		} else {
			throw new UserNotFoundException("no user with email: " + email + " found");
		}

	}

}
