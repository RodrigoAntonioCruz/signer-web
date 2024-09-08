package com.signer.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotFoundException extends BusinessException {

	@Serial
	private static final long serialVersionUID = 3823335406673292457L;
	
	public NotFoundException(String object) {
		super.setHttpStatusCode(HttpStatus.NOT_FOUND);
		super.setMessage(object);
	}
}