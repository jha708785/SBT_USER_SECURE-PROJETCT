package com.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailRequest() {

	
	 @Email
     @NotNull
     static String email;
}
