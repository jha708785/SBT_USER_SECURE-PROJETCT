package com.security.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPassword {

	@NotNull
	private String email;
}
