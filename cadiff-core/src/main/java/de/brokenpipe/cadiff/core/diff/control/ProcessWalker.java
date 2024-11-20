package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.diff.control.comparators.processes.ProcessNameComparator;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.Collection;
import java.util.stream.Stream;

public class ProcessWalker extends AbstractVoteAddWalker<Process> {

	public ProcessWalker(final Collection<Process> fromProcesses, final Collection<Process> toProcesses) {
		super(fromProcesses, toProcesses);
	}

	@Override
	protected Stream<Action> handleUpdated(final Process from, final Process to) {

		final var actions = new FlowElementWalker(from.getFlowElements(), to.getFlowElements()).walk().toList();

		return Stream.concat(
				actions.isEmpty()
						? Stream.empty()
						: Stream.of(new ChangeProcessAction(from.getId(), actions)),
				handleNameChange(from, to)
		);
	}

	private Stream<Action> handleNameChange(final Process from, final Process to) {
		return new ProcessNameComparator().apply(from, to);
	}

}
