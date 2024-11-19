package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeIdPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record ChangeIdAction(String oldId, String newId) implements Action, AddAction {

	@Override
	public Patcher patcher() {
		return new ChangeIdPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return List.of(newId);
	}

	@Override
	public List<String> idsRemoved() {
		return List.of(oldId);
	}
}
