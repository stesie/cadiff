package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddFlowAction;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Creator for finding new flows, e.g. new boundary events with multiple nodes following after it.
 */
public class AddFlowCreator implements Creator {

	@Override
	public Integer getPriority() {
		return Integer.valueOf(4490);
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<String, ? extends BaseElement> voteContext) {
		return findNewBoundaryEvents(voteContext)
				.flatMap(originFlowNode -> {
					final String originFlowNodeId = originFlowNode.getId();
					final var path = walk(voteContext, List.of(originFlowNodeId));

					if (path.isEmpty()) {
						return Stream.empty();
					}

					final var steps = path.get().stream().map(
									stepElementId -> {
										final var element = voteContext.toMap().get(stepElementId);

										if (element instanceof final FlowNode flowNode) {
											return new InsertNodeOnEdgeAction.Step(stepElementId,
													flowNode.getElementType().getTypeName(),
													Optional.of(Bounds.of(flowNode.getDiagramElement())),
													Optional.empty()
											);
										} else if (element instanceof final SequenceFlow sequenceFlow) {
											return new InsertNodeOnEdgeAction.Step(stepElementId,
													sequenceFlow.getElementType().getTypeName(),
													Optional.empty(),
													Optional.of(sequenceFlow.getDiagramElement().getWaypoints().stream()
															.map(Waypoint::of)
															.toList())
											);
										} else {
											throw new NotImplementedException();
										}
									})
							.toList();

					final AddAction action = new AddFlowAction(
							Optional.of(originFlowNode)
											.filter(BoundaryEvent.class::isInstance)
											.map(BoundaryEvent.class::cast)
											.map(BoundaryEvent::getAttachedTo)
											.map(BaseElement::getId),
							!voteContext.fromMap().containsKey(steps.getLast().id()), steps);
					return Stream.of(action);
				})
				.findFirst();
	}

	private Optional<List<String>> walk(final VoteContext<String, ? extends BaseElement> voteContext,
			final List<String> path) {
		final var lastElement = voteContext.toMap().get(path.getLast());
		final Stream<SequenceFlow> candidates;

		if (lastElement instanceof final FlowNode flowNode) {
			// outgoing flows that are new
			candidates = flowNode.getOutgoing().stream()
					.filter(sequenceFlow -> voteContext.added().contains(sequenceFlow.getId()))
			;
		} else {
			throw new NotImplementedException();
		}

		return candidates
				.flatMap(candidate -> {
					final var id = candidate.getTarget().getId();

					if (path.contains(id)) {
						return Stream.empty();
					}

					final var nextPath = new ArrayList<>(path);
					nextPath.add(candidate.getId());
					nextPath.add(id);

					final var targetElement = voteContext.toMap().get(id);

					if (!voteContext.added().contains(id) || targetElement instanceof ThrowEvent) {
						return Stream.of(nextPath);
					}

					return walk(voteContext, nextPath).stream();
				})
				.min(Comparator.comparing(List::size));
	}

	/**
	 * Find newly added flow nodes that are at the start of a flow (e.g. a boundary event or a process start event).
	 */
	private Stream<FlowNode> findNewBoundaryEvents(final VoteContext<String, ? extends BaseElement> voteContext) {
		return voteContext.added().stream()
				.map(id -> voteContext.toMap().get(id))
				.filter(FlowNode.class::isInstance)
				.map(FlowNode.class::cast)
				// accept only nodes at the start of a (new) flow
				.filter(flowNode -> flowNode.getIncoming().isEmpty())
				// if the attachedTo element is still in the added list, defer creation of the boundary event
				.filter(flowNode -> !(flowNode instanceof final BoundaryEvent boundaryEvent)
						|| !voteContext.added().contains(boundaryEvent.getAttachedTo().getId()));
	}

}
