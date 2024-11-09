package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

public class PatchNotApplicableException extends RuntimeException {
	public PatchNotApplicableException(final String message) {
		super(message);
	}
}
