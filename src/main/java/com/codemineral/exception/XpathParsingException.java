package com.codemineral.exception;

public class XpathParsingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7643026441850239406L;

	public XpathParsingException(String msg) {
		super(msg);
	}

	public XpathParsingException() {
		super();
	}

	public XpathParsingException(String msg, Throwable t) {
		super(msg, t);
	}

	public XpathParsingException(Throwable t) {
		super(t);
	}
}
