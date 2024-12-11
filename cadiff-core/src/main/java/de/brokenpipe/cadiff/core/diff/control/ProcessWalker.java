package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.diff.control.comparators.processes.ProcessNameComparator;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.Collection;
import java.util.stream.Stream;

public class ProcessWalker extends AbstractVoteAddWalker<Process> {

	public ProcessWalker(final CompareContext<Collection<Process>> compareContext) {
		super(compareContext);
	}

	@Override
	protected Stream<Action> handleUpdated(final CompareContext<Process> updateContext) {


		final var actions = new FlowElementWalker(updateContext.map(Process::getFlowElements)).walk().toList();

		return Stream.concat(
				actions.isEmpty()
						? Stream.empty()
						: Stream.of(new ChangeProcessAction(updateContext.from().getId(), actions)),
				handleNameChange(updateContext)
		);
	}

	private Stream<Action> handleNameChange(final CompareContext<Process> updateContext) {
		return new ProcessNameComparator().apply(updateContext);
	}

}
