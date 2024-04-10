package com.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException{

	public EmailAlreadyExistsException() {
		super(HttpStatus.CONFLICT, "Email already exists");
	}

}
