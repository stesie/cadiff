package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeErrorNameAction;
import org.camunda.bpm.model.bpmn.instance.Error;

public class ChangeErrorNamePatcher extends AbstractChangePropertyPatcher<Error, String> {

	public ChangeErrorNamePatcher(final ChangeErrorNameAction action) {
		super(action, Error.class, Error::getName, Error::setName);
	}
}
