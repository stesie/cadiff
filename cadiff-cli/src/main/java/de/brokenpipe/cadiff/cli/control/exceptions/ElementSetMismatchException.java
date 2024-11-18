package de.brokenpipe.cadiff.cli.control.exceptions;

import lombok.Getter;

import java.util.Collection;

@Getter
public class ElementSetMismatchException extends SelftestException {

	private final Collection<String> missingIds;
	private final Collection<String> excessIds;

	public ElementSetMismatchException(final String elementId, final Collection<String> missingIds, final Collection<String> excessIds) {
		super("Number of flow elements mismatching in process (missing: %s / excess: %s)".formatted(missingIds, excessIds), elementId);
		this.missingIds = missingIds;
		this.excessIds = excessIds;
	}
}
