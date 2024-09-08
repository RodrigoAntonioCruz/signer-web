package com.signer.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericException extends BusinessException {

	@Serial
	private static final long serialVersionUID = 3823335406673292457L;

	public GenericException(String object) {
		super.setHttpStatusCode(HttpStatus.BAD_REQUEST);
		super.setMessage(object);
	}
}