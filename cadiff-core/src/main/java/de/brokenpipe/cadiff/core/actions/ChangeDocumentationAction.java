package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeDocumentationPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeDocumentationAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeDocumentationPatcher(this);
	}
}
