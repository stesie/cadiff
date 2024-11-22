package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaMapDecisionResultAction;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;

import java.util.stream.Stream;

public class CamundaMapDecisionResultComparator extends UpcastComparator<BusinessRuleTask>
		implements StringPropertyComparator<BusinessRuleTask> {

	@Override
	protected Class<BusinessRuleTask> getClassType() {
		return BusinessRuleTask.class;
	}

	@Override
	protected Stream<Action> compare(final BusinessRuleTask from, final BusinessRuleTask to) {
		return compareStringProperty(BusinessRuleTask::getCamundaMapDecisionResult,
				ChangeCamundaMapDecisionResultAction.class, from, to);
	}
}
