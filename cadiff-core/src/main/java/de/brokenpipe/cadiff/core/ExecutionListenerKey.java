package de.brokenpipe.cadiff.core;

import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

public record ExecutionListenerKey(String camundaEvent, String camundaClass,
								   String camundaDelegateExpression, String camundaExpression) {

	public static ExecutionListenerKey of(final CamundaExecutionListener listener) {
		return new ExecutionListenerKey(listener.getCamundaEvent(), listener.getCamundaClass(),
				listener.getCamundaDelegateExpression(), listener.getCamundaExpression());
	}
}