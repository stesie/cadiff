package de.brokenpipe.cadiff.core.diff.control.comparators.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessNameAction;
import de.brokenpipe.cadiff.core.diff.control.comparators.UpcastComparator;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareStringProperty;

public class ProcessNameComparator extends UpcastComparator<Process> {

	@Override
	protected Class<Process> getClassType() {
		return Process.class;
	}

	@Override
	public Stream<Action> compare(final CompareContext<Process> compareContext) {
		return compareStringProperty(Process::getName, ChangeProcessNameAction.class, compareContext);
	}

}
