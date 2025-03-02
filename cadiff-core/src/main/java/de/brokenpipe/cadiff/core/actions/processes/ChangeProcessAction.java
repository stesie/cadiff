package de.brokenpipe.cadiff.core.actions.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.processes.ChangeProcessPatcher;

import java.util.Collection;

public record ChangeProcessAction(String id, Collection<Action> actions)  implements Action {

	@Override
	public Patcher patcher() {
		return new ChangeProcessPatcher(this);
	}
}
