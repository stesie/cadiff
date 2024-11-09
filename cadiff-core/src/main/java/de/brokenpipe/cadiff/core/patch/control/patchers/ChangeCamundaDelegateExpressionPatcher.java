package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaDelegateExpressionAction;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

public class ChangeCamundaDelegateExpressionPatcher extends AbstractChangePropertyPatcher<ServiceTask, String> {

	public ChangeCamundaDelegateExpressionPatcher(final ChangeCamundaDelegateExpressionAction action) {
		super(action, ServiceTask.class, ServiceTask::getCamundaDelegateExpression, ServiceTask::setCamundaDelegateExpression);
	}
}
