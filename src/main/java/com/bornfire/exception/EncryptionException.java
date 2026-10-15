package com.bornfire.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EncryptionException extends RuntimeException{
	private static final long serialVersionUID = 1L;
    public EncryptionException(String message) {
        super(message);
    }
}
