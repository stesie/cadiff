package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.Optional;

public class SimpleFlowNodeCreator implements Creator {
	@Override
	public Integer getPriority() {
		return Integer.valueOf(-1000);
	}

	@Override
	public Optional<AddAction> apply(final String addId, final VoteContext<? extends BaseElement> voteContext) {
		final var addedElement = voteContext.toMap().get(addId);

		if (addedElement instanceof final FlowNode flowNode) {
			return Optional.of(
					new AddSimpleFlowNodeAction(flowNode.getId(), flowNode.getElementType().getTypeName(),
							Bounds.of(flowNode.getDiagramElement())));
		}

		return Optional.empty();
	}
}
