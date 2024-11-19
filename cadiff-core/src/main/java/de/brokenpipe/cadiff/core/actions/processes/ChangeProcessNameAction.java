package de.brokenpipe.cadiff.core.actions.processes;

import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.processes.ChangeProcessNamePatcher;

public record ChangeProcessNameAction(String id, String oldValue, String newValue) implements
		ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeProcessNamePatcher(this);
	}
}
