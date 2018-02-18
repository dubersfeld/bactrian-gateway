package com.dub.customers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomerServerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4130194812544485429L;

}
