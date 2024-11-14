package de.brokenpipe.cadiff.core.actions.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.processes.ChangeSubProcessPatcher;

import java.util.Collection;

public record ChangeSubProcessAction(String id, Collection<Action> actions)  implements Action {

	@Override
	public Patcher getPatcher() {
		return new ChangeSubProcessPatcher(this);
	}
}
