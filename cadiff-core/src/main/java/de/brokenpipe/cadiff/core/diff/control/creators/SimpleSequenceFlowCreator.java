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
	public Optional<AddAction> apply(final VoteContext<? extends BaseElement> voteContext) {
		return voteContext.added().stream()
				.map(addId -> voteContext.toMap().get(addId))
				.filter(SequenceFlow.class::isInstance)
				.map(SequenceFlow.class::cast)
				.map(sequenceFlow -> {
					// noinspection UnnecessaryLocalVariable
					final AddAction addAction = new AddSimpleSequenceFlowAction(
							sequenceFlow.getId(),
							sequenceFlow.getSource().getId(),
							sequenceFlow.getTarget().getId(),
							sequenceFlow.getDiagramElement().getWaypoints().stream().map(Waypoint::of).toList());
					return addAction;
				})
				.findFirst();
	}
}
