package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaExpressionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCamundaExpressionAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaExpressionPatcher(this);
	}
}
