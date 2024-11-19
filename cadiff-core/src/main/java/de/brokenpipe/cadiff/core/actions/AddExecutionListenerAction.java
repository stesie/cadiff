package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.AddExecutionListenerPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;

public record AddExecutionListenerAction(String id, String camundaEvent, String camundaClass,
										 String camundaDelegateExpression, String camundaExpression) implements Action {
	@Override
	public Patcher getPatcher() {
		return new AddExecutionListenerPatcher(this);
	}
}
