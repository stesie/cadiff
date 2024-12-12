package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericChangePropertyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.Error;

public record ChangeErrorNameAction(String id, String oldValue, String newValue) implements
		ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new GenericChangePropertyPatcher<>(this, Error.class, Error::getName, Error::setName);
	}
}
