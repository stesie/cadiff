package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeSubProcessAction;
import de.brokenpipe.cadiff.core.diff.control.FlowElementWalker;
import org.camunda.bpm.model.bpmn.instance.SubProcess;

import java.util.stream.Stream;

public class SubProcessComparator extends UpcastComparator<SubProcess> {
	@Override
	protected Class<SubProcess> getClassType() {
		return SubProcess.class;
	}

	@Override
	protected Stream<Action> compare(final SubProcess from, final SubProcess to) {

		// FIXME handle add & delete !?

		final var actions =  new FlowElementWalker(from.getFlowElements(), to.getFlowElements()).walk().toList();

		if (actions.isEmpty()) {
			return Stream.empty();
		}

		return Stream.of(new ChangeSubProcessAction(from.getId(), actions));
	}
}
