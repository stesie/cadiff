package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.Optional;

public class SimpleSequenceFlowCreator implements Creator {
	@Override
	public Integer getPriority() {
		return Integer.valueOf(-1010);
	}

	@Override
	public Optional<AddAction> apply(final String addId, final VoteContext<? extends BaseElement> voteContext) {
		final var addedElement = voteContext.toMap().get(addId);

		if (addedElement instanceof final SequenceFlow sequenceFlow) {
			return Optional.of(
					new AddSimpleSequenceFlowAction(
							sequenceFlow.getId(),
							sequenceFlow.getSource().getId(),
							sequenceFlow.getTarget().getId(),
							sequenceFlow.getDiagramElement().getWaypoints().stream().map(Waypoint::of).toList()));
		}

		return Optional.empty();
	}
}
