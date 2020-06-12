package com.rmendes.register.exceptions;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class NegativeBalanceOnCreationException extends RuntimeException{

	private static final long serialVersionUID = -881104505772360031L;
	
	@ConfigProperty(name = "negative.balance.exception.message")
	private static String message;
	
	public NegativeBalanceOnCreationException() {
		super(message);
	}

}
