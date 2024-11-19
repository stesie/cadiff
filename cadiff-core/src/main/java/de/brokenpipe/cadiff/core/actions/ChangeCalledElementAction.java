package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCalledElementPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCalledElementAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeCalledElementPatcher(this);
	}
}
