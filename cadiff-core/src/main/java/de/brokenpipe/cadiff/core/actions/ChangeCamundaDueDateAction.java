package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaDueDatePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCamundaDueDateAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeCamundaDueDatePatcher(this);
	}
}
