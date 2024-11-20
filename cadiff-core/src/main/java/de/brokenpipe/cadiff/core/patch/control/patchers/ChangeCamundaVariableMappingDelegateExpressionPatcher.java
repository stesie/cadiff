package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaVariableMappingDelegateExpressionAction;
import org.camunda.bpm.model.bpmn.instance.CallActivity;

public class ChangeCamundaVariableMappingDelegateExpressionPatcher extends AbstractChangePropertyPatcher<CallActivity, String> {

	public ChangeCamundaVariableMappingDelegateExpressionPatcher(final ChangeCamundaVariableMappingDelegateExpressionAction action) {
		super(action, CallActivity.class, CallActivity::getCamundaVariableMappingDelegateExpression, CallActivity::setCamundaVariableMappingDelegateExpression);
	}
}
