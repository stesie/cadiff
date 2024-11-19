package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeCamundaExclusivePatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record ChangeCamundaExclusiveAction(String id, Boolean oldValue, Boolean newValue) implements ChangePropertyAction<Boolean> {

	@Override
	public Patcher getPatcher() {
		return new ChangeCamundaExclusivePatcher(this);
	}
}
