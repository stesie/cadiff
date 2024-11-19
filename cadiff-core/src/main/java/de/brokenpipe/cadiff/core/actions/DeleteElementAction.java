package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.DeleteElementPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record DeleteElementAction(String id) implements Action {
	@Override
	public Patcher patcher() {
		return new DeleteElementPatcher(this);
	}
}
