package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaExpressionAction;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

public class ChangeCamundaExpressionPatcher extends AbstractChangePropertyPatcher<ServiceTask, String> {

	public ChangeCamundaExpressionPatcher(final ChangeCamundaExpressionAction action) {
		super(action, ServiceTask.class, ServiceTask::getCamundaExpression, ServiceTask::setCamundaExpression);
	}
}
