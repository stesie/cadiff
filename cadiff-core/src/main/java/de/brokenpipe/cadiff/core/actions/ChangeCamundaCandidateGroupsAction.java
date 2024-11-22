package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaCandidateGroupsPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCamundaCandidateGroupsAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeCamundaCandidateGroupsPatcher(this);
	}
}
