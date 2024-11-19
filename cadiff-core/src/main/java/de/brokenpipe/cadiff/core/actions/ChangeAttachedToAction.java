package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeAttachedToPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeAttachedToAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher getPatcher() {
		return new ChangeAttachedToPatcher(this);
	}
}
