package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeIdPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;

import java.util.List;

public record ChangeIdAction(String oldId, String newId) implements Action, AddAction {

	@Override
	public Patcher getPatcher() {
		return new ChangeIdPatcher(this);
	}

	@Override
	public List<String> getIdsAdded() {
		return List.of(newId);
	}

	@Override
	public List<String> getIdsRemoved() {
		return List.of(oldId);
	}
}
