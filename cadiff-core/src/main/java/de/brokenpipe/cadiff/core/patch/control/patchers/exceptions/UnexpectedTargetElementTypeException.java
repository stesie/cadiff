package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import lombok.Getter;

@Getter
public class UnexpectedTargetElementTypeException extends PatchNotApplicableException {

	private final String elementId;
	private final String actualType;
	private final String expectedType;

	public UnexpectedTargetElementTypeException(final String elementId, final String actualType, final String expectedType) {
		super("Unexpected target element type on element '%s'. Expected %s, found: %s".formatted(elementId, expectedType, actualType));

		this.elementId = elementId;
		this.actualType = actualType;
		this.expectedType = expectedType;
	}
}
