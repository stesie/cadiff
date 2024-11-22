package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaDecisionRefAction;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;

public class ChangeCamundaDecisionRefPatcher extends AbstractChangePropertyPatcher<BusinessRuleTask, String> {

	public ChangeCamundaDecisionRefPatcher(final ChangeCamundaDecisionRefAction action) {
		super(action, BusinessRuleTask.class, BusinessRuleTask::getCamundaDecisionRef, BusinessRuleTask::setCamundaDecisionRef);
	}
}
