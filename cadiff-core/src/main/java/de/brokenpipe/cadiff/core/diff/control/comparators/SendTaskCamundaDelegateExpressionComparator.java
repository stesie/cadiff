package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaDelegateExpressionAction;
import org.camunda.bpm.model.bpmn.instance.SendTask;

import java.util.stream.Stream;

public class SendTaskCamundaDelegateExpressionComparator extends UpcastComparator<SendTask>
		implements StringPropertyComparator<SendTask> {

	@Override
	protected Class<SendTask> getClassType() {
		return SendTask.class;
	}

	@Override
	protected Stream<Action> compare(final SendTask from, final SendTask to) {
		return compareStringProperty(SendTask::getCamundaDelegateExpression,
				ChangeCamundaDelegateExpressionAction.class, from, to);
	}
}
