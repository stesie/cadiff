package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import lombok.Getter;

@Getter
public class NameAlreadyPresentException extends PatchNotApplicableException {

	private final String elementId;
	private final String name;

	public NameAlreadyPresentException(final String elementId, final String name) {
		super("Name '%s' already present on element '%s'".formatted(name, elementId));

		this.elementId = elementId;
		this.name = name;
	}
}
