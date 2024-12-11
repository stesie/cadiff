package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaDecisionRefAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;

import java.util.stream.Stream;

public class CamundaDecisionRefComparator extends UpcastComparator<BusinessRuleTask>
		implements StringPropertyComparator<BusinessRuleTask> {

	@Override
	protected Class<BusinessRuleTask> getClassType() {
		return BusinessRuleTask.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<BusinessRuleTask> compareContext) {
		return compareStringProperty(BusinessRuleTask::getCamundaDecisionRef,
				ChangeCamundaDecisionRefAction.class, compareContext);
	}
}
