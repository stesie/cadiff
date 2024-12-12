package de.brokenpipe.cadiff.core.actions.processes;

import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.GenericChangePropertyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.Process;

public record ChangeProcessNameAction(String id, String oldValue, String newValue) implements
		ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new GenericChangePropertyPatcher<>(this, Process.class, Process::getName, Process::setName);
	}
}
