package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeSequenceFlowAction;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.stream.Stream;

public class SequenceFlowComparator extends UpcastComparator<SequenceFlow> {
	@Override
	protected Class<SequenceFlow> getClassType() {
		return SequenceFlow.class;
	}

	@Override
	protected Stream<Action> compare(final SequenceFlow from, final SequenceFlow to) {

		final String oldSourceId = from.getSource().getId();
		final String newSourceId = to.getSource().getId();

		final String oldTargetId = from.getTarget().getId();
		final String newTargetId = to.getTarget().getId();

		if (oldSourceId.equals(newSourceId) && oldTargetId.equals(newTargetId)) {
			return Stream.empty();
		}
		return Stream.of(new ChangeSequenceFlowAction(from.getId(), oldSourceId, newSourceId, oldTargetId, newTargetId));
	}
}
