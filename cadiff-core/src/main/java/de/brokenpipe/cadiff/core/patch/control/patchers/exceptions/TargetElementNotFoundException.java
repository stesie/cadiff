package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import lombok.Getter;

@Getter
public class TargetElementNotFoundException extends PatchNotApplicableException {

	private final String elementId;

	public TargetElementNotFoundException(final String elementId) {
		super("Target element not found: %s".formatted(elementId));

		this.elementId = elementId;
	}
}
