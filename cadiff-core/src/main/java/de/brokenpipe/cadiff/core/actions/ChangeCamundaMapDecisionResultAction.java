package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericChangePropertyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;

public record ChangeCamundaMapDecisionResultAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new GenericChangePropertyPatcher<>(this, BusinessRuleTask.class, BusinessRuleTask::getCamundaMapDecisionResult, BusinessRuleTask::setCamundaMapDecisionResult);
	}
}
