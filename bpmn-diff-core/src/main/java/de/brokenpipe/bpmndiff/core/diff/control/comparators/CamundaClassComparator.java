package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.Action;
import de.brokenpipe.bpmndiff.core.actions.ChangeCamundaClassAction;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

import java.util.stream.Stream;

public class CamundaClassComparator extends UpcastComperator<ServiceTask>
		implements StringPropertyComparator<ServiceTask> {

	@Override
	protected Class<ServiceTask> getClassType() {
		return ServiceTask.class;
	}

	@Override
	protected Stream<Action> compare(final ServiceTask from, final ServiceTask to) {
		return compareStringProperty(ServiceTask::getCamundaClass,
				ChangeCamundaClassAction.class, from, to);
	}
}
