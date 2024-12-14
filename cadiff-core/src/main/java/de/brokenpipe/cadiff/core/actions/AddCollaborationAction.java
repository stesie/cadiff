package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddCollaborationPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddCollaborationAction(String id) implements AddAction  {
	@Override
	public Patcher patcher() {
		return new AddCollaborationPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return List.of(id);
	}
}
