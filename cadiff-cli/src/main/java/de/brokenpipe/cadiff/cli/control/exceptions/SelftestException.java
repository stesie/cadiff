package de.brokenpipe.cadiff.cli.control.exceptions;

public class SelftestException extends RuntimeException {

	public SelftestException(final String message, final String elementId) {
		super(message + " (id: " + elementId + ")");
	}

	public SelftestException(final String message, final String path, final String expected, final String actual) {
		super(message + " (path: " + path + ", expected: " + expected + ", actual: " + actual + ")");
	}
}
