package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaCandidateGroupsAction;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.util.stream.Stream;

public class CamundaCandidateGroupsComparator extends UpcastComparator<UserTask>
		implements StringPropertyComparator<UserTask> {

	@Override
	protected Class<UserTask> getClassType() {
		return UserTask.class;
	}

	@Override
	protected Stream<Action> compare(final UserTask from, final UserTask to) {
		return compareStringProperty(UserTask::getCamundaCandidateGroups,
				ChangeCamundaCandidateGroupsAction.class, from, to);
	}
}
