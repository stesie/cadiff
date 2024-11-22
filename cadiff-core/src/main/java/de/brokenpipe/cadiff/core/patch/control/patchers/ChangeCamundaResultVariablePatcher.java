package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaResultVariableAction;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;

public class ChangeCamundaResultVariablePatcher extends AbstractChangePropertyPatcher<BusinessRuleTask, String> {

	public ChangeCamundaResultVariablePatcher(final ChangeCamundaResultVariableAction action) {
		super(action, BusinessRuleTask.class, BusinessRuleTask::getCamundaResultVariable, BusinessRuleTask::setCamundaResultVariable);
	}
}
