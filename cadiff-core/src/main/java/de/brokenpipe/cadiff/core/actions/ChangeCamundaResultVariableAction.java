package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaResultVariablePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCamundaResultVariableAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeCamundaResultVariablePatcher(this);
	}
}
