package de.brokenpipe.cadiff.core.diff.control.comparators.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessNameAction;
import de.brokenpipe.cadiff.core.diff.control.comparators.StringPropertyComparator;
import de.brokenpipe.cadiff.core.diff.control.comparators.UpcastComperator;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.stream.Stream;

public class ProcessNameComparator extends UpcastComperator<Process> implements StringPropertyComparator<Process> {

	@Override
	protected Class<Process> getClassType() {
		return Process.class;
	}

	@Override
	public Stream<Action> compare(final Process from, final Process to) {
		return compareStringProperty(Process::getName, ChangeProcessNameAction.class, from, to);
	}

}
