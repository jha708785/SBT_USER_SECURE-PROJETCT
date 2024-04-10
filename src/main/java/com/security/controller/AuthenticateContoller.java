package com.security.controller;

import com.security.exception.EmailAlreadyExistsException;
import com.security.exception.PasswordDontMatchException;
import com.security.exception.UserNotFoundException;
import com.security.jwt.JwtProviders;
import com.security.request.LoginRequest;
import com.security.request.RegesterRequest;
import com.security.request.updatepassword;
import com.security.response.AuthResponse;
import com.security.service.AuthenticationService;
import com.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticateContoller {

	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private UserService userService;

	@Autowired
	private JwtProviders providers;

	@PostMapping("register")
	public ResponseEntity<String> register(@Valid @RequestBody RegesterRequest request) {
		try {
			authenticationService.regester(request);
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
		} catch (PasswordDontMatchException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and password confirm do not match");
		} catch (EmailAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
		} catch (MailSendException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while sending activation link");
		} 
			 catch (Exception e) { return
			 ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
			 body("Error while registering user"); }
			 
	}

	@PostMapping("enable-user/{token}")
	public ResponseEntity<String> enableUser(@PathVariable String token) {
		try {
			authenticationService.veryfy(token);
			// If the enableUser method succeeds, perform the redirect
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Location", "http://localhost:5173/login")
					.body(null);
		}

		catch (UserNotFoundException e) { // UserNotFoundException is a custom exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found !!");
		} catch (Exception e) { // For any other unhandled exceptions, return a generic error message
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to enable user.");
		}
	}



	@PostMapping("/login")
	public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest logReq )throws UserNotFoundException{

		String email = logReq.getEmail();
		String password = logReq.getPassword();


		Authentication authenticte = authenticationService.Authenticte(email, password);
		SecurityContextHolder.getContext().setAuthentication(authenticte);
		String generateToken = providers.generateToken(authenticte);

		AuthResponse response=new AuthResponse();
		response.setJwt(generateToken);
		response.setMessage("sign successfully...!!");

		return new ResponseEntity<AuthResponse>(response, HttpStatus.CREATED);
	}


	@PostMapping ("update")
	public ResponseEntity<String>updatepassword(@RequestBody updatepassword pass){
		try {
			authenticationService.updatepassword(pass.getEmail(),
					pass.getPassword(),
					pass.getCnfpassword());
			return ResponseEntity.status(HttpStatus.CREATED).body("User update successfully");
		}
		catch (PasswordDontMatchException e){
			return ResponseEntity.status(HttpStatus.CREATED).body("User password dont match");
		}
		catch (UserNotFoundException e){
			return ResponseEntity.status(HttpStatus.CREATED).body("User not  found");
		}
		catch (Exception e){
			return ResponseEntity.status(HttpStatus.CREATED).body("Some thing errror ");
		}




	}

	/*@PostMapping("reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody @Valid updatepassword request) {

		try {
			authenticationService.upDatePassword(request.getPassword(), request.getCnfpassword());
			// If the upDatePassword method succeeds, perform the redirect
			return ResponseEntity.status(HttpStatus.SC_OK).body("Password updated successfully");
		} catch (PasswordDontMatchException e) { // PasswordDontMatchException is a custom exception
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Password don't match !!");
		}

		catch (UserNotFoundException e) { // UserNotFoundException is a custom exception
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("User not found !!");
		} catch (Exception e) { // For any other unhandled exceptions, return a generic error message
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Failed to reset password try again !!");
		}
	}*/


}
