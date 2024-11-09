package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.diff.control.comparators.processes.ProcessNameComparator;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ProcessWalker extends AbstractWalker<Process> {

	public ProcessWalker(final Collection<Process> fromProcesses, final Collection<Process> toProcesses) {
		super(fromProcesses, toProcesses);
	}

	@Override
	protected Stream<Action> handleUpdated(final Process from, final Process to) {
		final List<Action> actions = new ArrayList<>();
		actions.addAll(handleNameChange(from, to).toList());
		actions.addAll(new FlowElementWalker(from.getFlowElements(), to.getFlowElements()).walk().toList());

		return Stream.of(new ChangeProcessAction(from.getId(), actions));
	}

	private Stream<Action> handleNameChange(final Process from, final Process to) {
		return new ProcessNameComparator().apply(from, to);
	}

	@Override
	protected Stream<Action> handleAdded(final Process added) {
		throw new NotImplementedException();
	}
}
