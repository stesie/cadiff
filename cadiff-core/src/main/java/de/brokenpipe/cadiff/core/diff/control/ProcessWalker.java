package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeProcessAction;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.Collection;
import java.util.stream.Stream;

public class ProcessWalker extends AbstractWalker<Process> {

	public ProcessWalker(final Collection<Process> fromProcesses, final Collection<Process> toProcesses) {
		super(fromProcesses, toProcesses);
	}

	@Override
	protected Stream<Action> handleUpdated(final Process from, final Process to) {
		return Stream.of(new ChangeProcessAction(
				from.getId(),
				new FlowElementWalker(from.getFlowElements(), to.getFlowElements()).walk().toList()));
	}

	@Override
	protected Stream<Action> handleAdded(final Process added) {
		throw new NotImplementedException();
	}
}
