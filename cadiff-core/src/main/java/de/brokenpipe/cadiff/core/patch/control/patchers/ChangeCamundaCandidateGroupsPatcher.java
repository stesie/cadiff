package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaCandidateGroupsAction;
import org.camunda.bpm.model.bpmn.instance.UserTask;

public class ChangeCamundaCandidateGroupsPatcher extends AbstractChangePropertyPatcher<UserTask, String> {

	public ChangeCamundaCandidateGroupsPatcher(final ChangeCamundaCandidateGroupsAction action) {
		super(action, UserTask.class, UserTask::getCamundaCandidateGroups, UserTask::setCamundaCandidateGroups);
	}
}
