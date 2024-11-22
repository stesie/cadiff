package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaDelegateExpressionAction;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

import java.util.stream.Stream;

public class ServiceTaskCamundaDelegateExpressionComparator extends UpcastComparator<ServiceTask>
		implements StringPropertyComparator<ServiceTask> {

	@Override
	protected Class<ServiceTask> getClassType() {
		return ServiceTask.class;
	}

	@Override
	protected Stream<Action> compare(final ServiceTask from, final ServiceTask to) {
		return compareStringProperty(ServiceTask::getCamundaDelegateExpression,
				ChangeCamundaDelegateExpressionAction.class, from, to);
	}
}
