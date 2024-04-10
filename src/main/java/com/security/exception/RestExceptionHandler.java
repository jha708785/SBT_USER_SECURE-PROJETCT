package com.security.exception;


import java.util.List;

import org.hibernate.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		List<String> errorList = exception.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
		response.setMessage(errorList.toString());
		return buildResponseEntity(response);
	}

	@ExceptionHandler(TypeMismatchException.class)
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(exception.getMessage());
		return buildResponseEntity(response);
	}

	@ExceptionHandler(PasswordDontMatchException.class)
	public ResponseEntity<Object> handlePasswordDontMatchException(PasswordDontMatchException exception) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(exception.getReason());
		return buildResponseEntity(response);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(exception.getReason());
		return buildResponseEntity(response);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(HttpStatus.CONFLICT);
		response.setMessage(exception.getReason());
		return buildResponseEntity(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAnyException(Exception exception) {
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setMessage("An internal server error has occurred." + exception.getMessage());
		return buildResponseEntity(error);
	}

	private ResponseEntity<Object> buildResponseEntity(ErrorResponse response) {
		return new ResponseEntity<>(response, response.getStatus());
	}
}
