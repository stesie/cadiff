package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaVariableMappingDelegateExpressionAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.CallActivity;

import java.util.stream.Stream;

public class CamundaVariableMappingDelegateExpressionComparator extends UpcastComparator<CallActivity>
		implements StringPropertyComparator<CallActivity> {

	@Override
	protected Class<CallActivity> getClassType() {
		return CallActivity.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<CallActivity> compareContext) {
		return compareStringProperty(CallActivity::getCamundaVariableMappingDelegateExpression,
				ChangeCamundaVariableMappingDelegateExpressionAction.class, compareContext);
	}
}
