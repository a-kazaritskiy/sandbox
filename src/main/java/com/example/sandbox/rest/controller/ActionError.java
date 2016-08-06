package com.example.sandbox.rest.controller;

public class ActionError {

	private final String code;
	private final String message;

	public static ActionError of(String code, Exception e) {
		return new ActionError(code, e.getMessage());
	}

	public static ActionError of(String code, String message) {
		return new ActionError(code, message);
	}

	private ActionError(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}