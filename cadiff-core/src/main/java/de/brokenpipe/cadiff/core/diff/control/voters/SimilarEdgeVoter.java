package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.stream.Collectors;

/**
 * Voter that votes up, if one of the incoming or outgoing edges have the same id.
 */
public class SimilarEdgeVoter implements Voter {

	@Override
	public Vote apply(final String removeId, final String addId, final VoteContext<String, ? extends BaseElement> context) {
		final var removed = context.fromMap().get(removeId);
		final var added = context.toMap().get(addId);

		if (removed instanceof final FlowNode removedFlowNode && added instanceof final FlowNode addedFlowNode) {
			final var incomingRemoved = removedFlowNode.getIncoming().stream()
					.map(BaseElement::getId).collect(Collectors.toSet());
			final var hasIncomingMatch = addedFlowNode.getIncoming().stream()
					.map(BaseElement::getId)
					.anyMatch(incomingRemoved::contains);
			
			if (hasIncomingMatch) {
				return Vote.UP;
			}

			final var outgoingRemoved = removedFlowNode.getOutgoing().stream()
					.map(BaseElement::getId).collect(Collectors.toSet());
			final var hasOutgoingMatch = addedFlowNode.getOutgoing().stream()
					.map(BaseElement::getId)
					.anyMatch(outgoingRemoved::contains);

			return hasOutgoingMatch ? Vote.UP : Vote.DOWN;
		}

		return Vote.NEUTRAL;
	}

}
