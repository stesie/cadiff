package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.ExecutionListenerKey;
import de.brokenpipe.cadiff.core.patch.control.patchers.DeleteExecutionListenerPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record DeleteExecutionListenerAction(String id, ExecutionListenerKey key) implements Action {
	@Override
	public Patcher patcher() {
		return new DeleteExecutionListenerPatcher(this);
	}
}
