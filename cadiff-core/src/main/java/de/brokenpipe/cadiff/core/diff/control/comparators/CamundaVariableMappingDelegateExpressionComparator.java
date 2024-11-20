package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaVariableMappingDelegateExpressionAction;
import org.camunda.bpm.model.bpmn.instance.CallActivity;

import java.util.stream.Stream;

public class CamundaVariableMappingDelegateExpressionComparator extends UpcastComparator<CallActivity>
		implements StringPropertyComparator<CallActivity> {

	@Override
	protected Class<CallActivity> getClassType() {
		return CallActivity.class;
	}

	@Override
	protected Stream<Action> compare(final CallActivity from, final CallActivity to) {
		return compareStringProperty(CallActivity::getCamundaVariableMappingDelegateExpression,
				ChangeCamundaVariableMappingDelegateExpressionAction.class, from, to);
	}
}
