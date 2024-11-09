package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeIdPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

public record ChangeIdAction(String oldId, String newId) implements Action {

	@Override
	public Patcher getPatcher() {
		return new ChangeIdPatcher(this);
	}
}
