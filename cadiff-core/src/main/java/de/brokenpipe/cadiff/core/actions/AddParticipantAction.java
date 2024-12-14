package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddParticipantPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

import java.util.List;

public record AddParticipantAction(String id) implements AddAction  {
	@Override
	public Patcher patcher() {
		return new AddParticipantPatcher(this);
	}

	@Override
	public List<String> idsAdded() {
		return List.of(id);
	}
}
