package com.example.sandbox.rest.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionResult<T> {

	private final T result;
	private final ActionError error;

	public ActionResult(T result) {
		this.result = result;
		this.error = null;
	}

	public ActionResult(ActionError error) {
		this.result = null;
		this.error = error;
	}

	public T getResult() {
		return result;
	}

	public ActionError getError() {
		return error;
	}
}