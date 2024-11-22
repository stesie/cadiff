package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddTaskListenerPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record AddTaskListenerAction(String id, String camundaEvent, String camundaClass,
										 String camundaDelegateExpression, String camundaExpression) implements Action {

	@Override
	public Patcher patcher() {
		return new AddTaskListenerPatcher(this);
	}
}
