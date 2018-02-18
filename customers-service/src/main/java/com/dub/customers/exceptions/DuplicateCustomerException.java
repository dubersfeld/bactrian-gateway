package com.dub.customers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DuplicateCustomerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 733870736300653196L;

}
