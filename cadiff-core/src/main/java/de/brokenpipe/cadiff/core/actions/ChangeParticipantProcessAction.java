package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeParticipantProcessPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeParticipantProcessAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeParticipantProcessPatcher(this);
	}
}
