package com.security.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class updatepassword {

	@NotNull(message = "Password shouldn't be null ")
	@Length(min = 8, max = 16, message = "password length should be more than 8 and less than 16")
	private String password;
	@NotNull(message = "Password Confirm shouldn't be null ")
	private String cnfpassword;

	private String email;
	//private String vcode;
}
