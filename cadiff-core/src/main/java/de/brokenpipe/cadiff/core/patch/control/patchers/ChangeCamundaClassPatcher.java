package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaClassAction;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

public class ChangeCamundaClassPatcher extends AbstractChangePropertyPatcher<ServiceTask, String> {

	public ChangeCamundaClassPatcher(final ChangeCamundaClassAction action) {
		super(action, ServiceTask.class, ServiceTask::getCamundaClass, ServiceTask::setCamundaClass);
	}
}
