package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.Optional;

public class SimpleFlowNodeCreator implements Creator {
	@Override
	public Integer getPriority() {
		return Integer.valueOf(-1000);
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<String, ? extends BaseElement> voteContext) {
		return voteContext.added().stream()
				.map(addId -> voteContext.toMap().get(addId))
				.filter(FlowNode.class::isInstance)
				.map(FlowNode.class::cast)
				// if this is a boundaryEvent, make sure the underlying activity is no longer on the add list;
				// so we maybe create the underlying activity here, but let AddFlowCreator handle the boundary event.
				.filter(flowNode -> !(flowNode instanceof final BoundaryEvent boundaryEvent)
					|| boundaryEvent.getAttachedTo() == null
					|| !voteContext.added().contains(boundaryEvent.getAttachedTo().getId()))
				.map(flowNode -> {
					// noinspection UnnecessaryLocalVariable
					final AddAction addAction = new AddSimpleFlowNodeAction(flowNode.getId(),
							flowNode.getElementType().getTypeName(),
							Bounds.of(flowNode.getDiagramElement()));
					return addAction;
				})
				.findFirst();
	}
}
