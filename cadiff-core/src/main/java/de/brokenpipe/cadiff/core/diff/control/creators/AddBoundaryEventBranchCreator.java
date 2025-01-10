package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddBoundaryEventBranchAction;
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
 * Creator for finding new boundary events with + flows following from it
 */
public class AddBoundaryEventBranchCreator implements Creator {

	@Override
	public Integer getPriority() {
		return Integer.valueOf(4490);
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<String, ? extends BaseElement> voteContext) {
		return findNewBoundaryEvents(voteContext)
				.flatMap(boundaryEvent -> {
					final String boundaryEventId = boundaryEvent.getId();
					final var path = walk(voteContext, List.of(boundaryEventId));

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

					final AddAction action = new AddBoundaryEventBranchAction(boundaryEvent.getAttachedTo().getId(),
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
	 * Find newly added outgoing branches from updated gateways.
	 */
	private Stream<BoundaryEvent> findNewBoundaryEvents(final VoteContext<String, ? extends BaseElement> voteContext) {
		return voteContext.added().stream()
				.map(id -> voteContext.toMap().get(id))
				.filter(BoundaryEvent.class::isInstance)
				.map(BoundaryEvent.class::cast);
	}

}
