package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import lombok.Getter;

@Getter
public class NameValueMismatchException extends PatchNotApplicableException {

	private final String elementId;
	private final String name;
	private final Object actualValue;
	private final Object expectedValue;

	public NameValueMismatchException(final String elementId, final String name, final Object actualValue, final Object expectedValue) {
		super("Mismatching (old) value on element '%s' with name '%s'. Expected '%s', found: '%s'".formatted(elementId, name, expectedValue, actualValue));

		this.elementId = elementId;
		this.name = name;
		this.actualValue = actualValue;
		this.expectedValue = expectedValue;
	}
}
