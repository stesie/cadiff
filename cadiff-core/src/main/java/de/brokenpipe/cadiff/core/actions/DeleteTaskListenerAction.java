package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.DeleteTaskListenerPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record DeleteTaskListenerAction(String id, String camundaEvent, String camundaClass,
											String camundaDelegateExpression, String camundaExpression) implements Action {
	@Override
	public Patcher patcher() {
		return new DeleteTaskListenerPatcher(this);
	}
}
