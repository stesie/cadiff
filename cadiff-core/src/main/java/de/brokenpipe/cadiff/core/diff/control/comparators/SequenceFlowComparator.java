package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeSequenceFlowAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.stream.Stream;

public class SequenceFlowComparator extends UpcastComparator<SequenceFlow> {
	@Override
	protected Class<SequenceFlow> getClassType() {
		return SequenceFlow.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<SequenceFlow> compareContext) {

		final String oldSourceId = compareContext.from().getSource().getId();
		final String newSourceId = compareContext.to().getSource().getId();

		final String oldTargetId = compareContext.from().getTarget().getId();
		final String newTargetId = compareContext.to().getTarget().getId();

		if (oldSourceId.equals(newSourceId) && oldTargetId.equals(newTargetId)) {
			return Stream.empty();
		}

		return Stream.of(new ChangeSequenceFlowAction(compareContext.from().getId(), oldSourceId, newSourceId, oldTargetId, newTargetId));
	}
}
