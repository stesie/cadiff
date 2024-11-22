package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaDueDateAction;
import org.camunda.bpm.model.bpmn.instance.UserTask;

public class ChangeCamundaDueDatePatcher extends AbstractChangePropertyPatcher<UserTask, String> {

	public ChangeCamundaDueDatePatcher(final ChangeCamundaDueDateAction action) {
		super(action, UserTask.class, UserTask::getCamundaDueDate, UserTask::setCamundaDueDate);
	}
}
