package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeNamePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeNameAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeNamePatcher(this);
	}
}
