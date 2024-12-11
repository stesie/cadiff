package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaFormKeyAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.util.stream.Stream;

public class CamundaFormKeyComparator extends UpcastComparator<UserTask>
		implements StringPropertyComparator<UserTask> {

	@Override
	protected Class<UserTask> getClassType() {
		return UserTask.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<UserTask> compareContext) {
		return compareStringProperty(UserTask::getCamundaFormKey,
				ChangeCamundaFormKeyAction.class, compareContext);
	}
}
