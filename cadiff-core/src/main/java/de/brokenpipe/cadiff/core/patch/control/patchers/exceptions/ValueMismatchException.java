package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import lombok.Getter;

@Getter
public class ValueMismatchException extends PatchNotApplicableException {

	private final String elementId;
	private final Object actualValue;
	private final Object expectedValue;

	public ValueMismatchException(final String elementId, final Object actualValue, final Object expectedValue) {
		super("Mismatching (old) value on element '%s'. Expected '%s', found: '%s'".formatted(elementId, expectedValue, actualValue));

		this.elementId = elementId;
		this.actualValue = actualValue;
		this.expectedValue = expectedValue;
	}
}
