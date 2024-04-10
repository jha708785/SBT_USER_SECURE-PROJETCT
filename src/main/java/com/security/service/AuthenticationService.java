package com.security.service;

import com.security.entity.User;
import com.security.exception.EmailAlreadyExistsException;
import com.security.exception.PasswordDontMatchException;
import com.security.jwt.JwtProviders;
import com.security.repo.UserRepo;
import com.security.request.RegesterRequest;
import com.security.response.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

	@Autowired
	private CustomUserServiceImplemention impl;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserService userService;
	@Autowired
	private Emailservice emailservice;

	@Autowired
	private JwtProviders providers;

	@Autowired
	private UserRepo repo;

	public Authentication Authenticte(String email, String password) {
		UserDetails userDetails = impl.loadUserByUsername(email);
		if (userDetails == null) {
			throw new BadCredentialsException("invalid username ");
		}
		if (!encoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("invalid password ");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public void regester(RegesterRequest regesterRequest) {
		if (!isPasswordAndPasswordConfirmMatches(regesterRequest)) {
			// log.error("Password and password confirm doesn't match");
			System.out.print("Password and password confirm doesn't match");
			throw new PasswordDontMatchException();
		}

		if (userService.emailExists(regesterRequest.getEmail())) {
			 log.error("Email already exists");
			 
			System.out.print("Email already exists");
			throw new EmailAlreadyExistsException();
		}

		User user = new User();
		user.setFirstName(regesterRequest.getFirstName());
		user.setLastName(regesterRequest.getLastName());
		user.setEmail(regesterRequest.getEmail());
		user.setPassword(encoder.encode(regesterRequest.getPassword()));
		user.setConfirmPassword(encoder.encode(regesterRequest.getConfirmPassword()));
		user.setAccountNonLocked(false);
		user.setEnabled(false);

		user.setRole("USER_ROLE");
		System.out.println("user" + user);

		var saveuser = userService.saveUser(user);


		 /* Authentication authentication=new UsernamePasswordAuthenticationToken(saveuser.getEmail(), saveuser.getPassword());
		  SecurityContextHolder.getContext().setAuthentication(authentication);
		String token= providers.generateToken(authentication);*/
		var token = user.getVcode();
		String activationLink = "http://localhost:9098/api/v1/auth/enable-user/" + token;

		try {
			emailservice.sendActivationLink(regesterRequest.getEmail(), regesterRequest.getFirstName(), activationLink);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.print("error occur here");
		}
		AuthResponse resp = new AuthResponse();
		resp.setJwt(token);
		resp.setMessage("SignUp Success..!!");

	}

	public boolean veryfy(String code) {
		User user = userService.findVcodebyUser(code);
		if (user == null) {
			return false;
		} else {
			user.setAccountNonLocked(true);
			user.setEnabled(true);
			user.setVcode(null);
			repo.save(user);
			return true;

		}

	}

	public void updatepassword(String email, String password, String passwordConfirm) {

		// String tokens = UUID.randomUUID().toString();
		//String email = providers.getEmail(jwt);
//		String tokens = UUID.randomUUID().toString();
//
           //providers.getEmail(jwt);
	User user = userService.findUserByEmail(email);
//		user.setVcode(tokens);
//		repo.save(user);

		userService.updatePassword(email, passwordConfirm, password);

	}

	public void sendResetPasswordRequestToUser(String email) {
		// If an account with the given email already exists, throws an exception
		var user = userService.findUserByEmail(email);

		String savetokens = UUID.randomUUID().toString();

		user.setVcode(savetokens);
		repo.save(user);





		// create the link for the account activation & set the token as a param
		String resetPasswordLink = "http://localhost:9098/reset-password?token=" + user.getVcode();

		// Send activation link.
		try {
			log.info("Sending reset password link to user with email {}", email);
			emailservice.sendResetPasswordRequestToUser(email, user.getFirstName(), resetPasswordLink);
		} catch (Exception e) {
			log.warn("Error while sending reset password link to user with email {}", email);
			log.info(
					"If u didn't receive the email, due to the fact that we are in dev mode, we can pretend that the following link is sent : {}",
					resetPasswordLink);
			throw new MailSendException("Error while sending reset password link to user with email :" + email);
		}
		log.info("Reset password link sent to user with email {}", email);
	}

	public boolean isPasswordAndPasswordConfirmMatches(RegesterRequest registerRequest) {

		return registerRequest.getPassword()
				.equals(registerRequest.getConfirmPassword());
	}

}
