package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaClassAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

import java.util.stream.Stream;

public class CamundaClassComparator extends UpcastComparator<ServiceTask>
		implements StringPropertyComparator<ServiceTask> {

	@Override
	protected Class<ServiceTask> getClassType() {
		return ServiceTask.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<ServiceTask> compareContext) {
		return compareStringProperty(ServiceTask::getCamundaClass,
				ChangeCamundaClassAction.class, compareContext);
	}
}
