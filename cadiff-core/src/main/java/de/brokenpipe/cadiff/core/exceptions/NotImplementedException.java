package de.brokenpipe.cadiff.core.exceptions;

public class NotImplementedException extends RuntimeException {
	public NotImplementedException() {
		super("Not implemented");
	}

	public NotImplementedException(final String message) {
		super("Not implemented: " + message);
	}
}
