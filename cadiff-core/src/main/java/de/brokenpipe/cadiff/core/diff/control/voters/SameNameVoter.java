package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

public class SameNameVoter implements Voter {

	@Override
	public Vote apply(final String removeId, final String addId, final VoteContext<String, ? extends BaseElement> context) {
		final var removed = context.fromMap().get(removeId);
		final var added = context.toMap().get(addId);

		if (removed instanceof final FlowNode removedFlowNode && added instanceof final FlowNode addedFlowNode) {
			if (removedFlowNode.getName() == null && addedFlowNode.getName() == null) {
				return Vote.NEUTRAL;
			}

			if (removedFlowNode.getName() == null || addedFlowNode.getName() == null) {
				return Vote.DOWN;
			}

			return (removedFlowNode.getName().equals(addedFlowNode.getName()))
				? Vote.UP
				: Vote.DOWN;
		}

		return Vote.NEUTRAL;
	}

}
