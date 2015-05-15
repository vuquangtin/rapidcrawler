package com.codemineral.exception;

public class CssParsingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5687813471334064484L;

	public CssParsingException(String msg) {
		super(msg);
	}

	public CssParsingException() {
		super();
	}

	public CssParsingException(String msg, Throwable t) {
		super(msg, t);
	}

	public CssParsingException(Throwable t) {
		super(t);
	}
}
