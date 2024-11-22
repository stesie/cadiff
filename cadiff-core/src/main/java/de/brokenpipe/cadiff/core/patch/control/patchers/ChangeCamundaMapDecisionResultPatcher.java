package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaMapDecisionResultAction;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;

public class ChangeCamundaMapDecisionResultPatcher extends AbstractChangePropertyPatcher<BusinessRuleTask, String> {

	public ChangeCamundaMapDecisionResultPatcher(final ChangeCamundaMapDecisionResultAction action) {
		super(action, BusinessRuleTask.class, BusinessRuleTask::getCamundaMapDecisionResult, BusinessRuleTask::setCamundaMapDecisionResult);
	}
}
