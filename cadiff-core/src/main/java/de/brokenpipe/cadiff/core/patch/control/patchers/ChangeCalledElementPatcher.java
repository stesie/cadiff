package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCalledElementAction;
import org.camunda.bpm.model.bpmn.instance.CallActivity;

public class ChangeCalledElementPatcher extends AbstractChangePropertyPatcher<CallActivity, String> {

	public ChangeCalledElementPatcher(final ChangeCalledElementAction action) {
		super(action, CallActivity.class, CallActivity::getCalledElement, CallActivity::setCalledElement);
	}
}
