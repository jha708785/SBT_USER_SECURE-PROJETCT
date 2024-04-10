package com.security.request;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegesterRequest {



	@Length(min = 3, max = 16, message = "first name length should be less than 16 and more than 3 ")
	private String firstName;

	private String lastName;

	@Email(message = "Email should be valid")
	 @NotNull(message = "Email shouldn't be null")
	@Length(min = 3, message = "email length should be more than 10 ")
	private String email;

	 @NotNull(message = "Password shouldn't be null ")
	@Length(min = 8, max = 16, message = "password length should be more than 8 and less than 16")
	private String password;

	@NotNull
	String confirmPassword;

}
