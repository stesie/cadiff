package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaFormKeyAction;
import org.camunda.bpm.model.bpmn.instance.UserTask;

public class ChangeCamundaFormKeyPatcher extends AbstractChangePropertyPatcher<UserTask, String> {

	public ChangeCamundaFormKeyPatcher(final ChangeCamundaFormKeyAction action) {
		super(action, UserTask.class, UserTask::getCamundaFormKey, UserTask::setCamundaFormKey);
	}
}
