package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeErrorNamePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeErrorNameAction(String id, String oldValue, String newValue) implements
		ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeErrorNamePatcher(this);
	}
}
