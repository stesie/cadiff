package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import lombok.Getter;

@Getter
public class NameNotFoundException extends PatchNotApplicableException {

	private final String elementId;
	private final String name;

	public NameNotFoundException(final String elementId, final String name) {
		super("Name '%s' not present on element '%s', but expected".formatted(name, elementId));

		this.elementId = elementId;
		this.name = name;
	}
}
