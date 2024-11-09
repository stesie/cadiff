package de.brokenpipe.cadiff.core.patch.control.patchers.processes;

import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessNameAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.AbstractChangePropertyPatcher;
import org.camunda.bpm.model.bpmn.instance.Process;

public class ChangeProcessNamePatcher extends AbstractChangePropertyPatcher<Process, String> {

	public ChangeProcessNamePatcher(final ChangeProcessNameAction action) {
		super(action, Process.class, Process::getName, Process::setName);
	}
}
