package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericChangePropertyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

public record ChangeCamundaExpressionAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new GenericChangePropertyPatcher<>(this, ServiceTask.class, ServiceTask::getCamundaExpression, ServiceTask::setCamundaExpression);
	}
}
