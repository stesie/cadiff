package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.Bounds;
import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.*;
import java.util.stream.Stream;

/**
 * Creator for finding edges, that formerly directly connected two nodes, but now have a new node (or more) in between.
 */
public class InsertNodeOnEdgeCreator implements Creator {

	@Override
	public Integer getPriority() {
		return Integer.valueOf(5000);
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<String, ? extends BaseElement> voteContext) {
		return Stream.concat(
						findReplacedEdges(voteContext),
						findRecycledEdges(voteContext))
				.flatMap(removedEdge -> {
					final var path = collectPath(voteContext, removedEdge.getSource().getId(),
							removedEdge.getTarget().getId());

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

					if (steps.size() == 3) {
						// special case, this just an edge with a new id
						return Stream.of(new ChangeIdAction(removedEdge.getId(), steps.get(1).id()));
					}

					final AddAction action = new InsertNodeOnEdgeAction(removedEdge.getId(), steps);
					return Stream.of(action);
				})
				.findFirst();
	}

	private Optional<List<String>> collectPath(final VoteContext<String, ? extends BaseElement> voteContext,
			final String sourceId, final String targetId) {
		return walk(voteContext, List.of(sourceId), targetId);
	}

	private Optional<List<String>> walk(final VoteContext<String, ? extends BaseElement> voteContext,
			final List<String> path, final String targetId) {
		final var lastElement = voteContext.toMap().get(path.getLast());
		final Collection<SequenceFlow> candidates;

		if (lastElement instanceof final FlowNode flowNode) {
			candidates = flowNode.getOutgoing();
		} else {
			throw new NotImplementedException();
		}

		return candidates.stream()
				.flatMap(candidate -> {
					final var id = candidate.getTarget().getId();

					if (path.contains(id)) {
						return Stream.empty();
					}

					final var nextPath = new ArrayList<>(path);
					nextPath.add(candidate.getId());
					nextPath.add(id);

					if (id.equals(targetId)) {
						return Stream.of(nextPath);
					}

					if (!voteContext.added().contains(id)) {
						return Stream.empty();
					}

					return walk(voteContext, nextPath, targetId).stream();
				})
				.min(Comparator.comparing(List::size));
	}

	/**
	 * Find edges that have been removed, but connected two nodes before, that remain in the model.
	 */
	private Stream<SequenceFlow> findReplacedEdges(final VoteContext<String, ? extends BaseElement> voteContext) {
		return voteContext.removed().stream()
				.map(removedId -> voteContext.fromMap().get(removedId))
				.filter(SequenceFlow.class::isInstance)
				.map(SequenceFlow.class::cast)
				.filter(removedEdge -> voteContext.toMap().containsKey(removedEdge.getSource().getId())
						&& voteContext.toMap().containsKey(removedEdge.getTarget().getId()));
	}

	/**
	 * Find <em>updated</em> edges with source or target changed, but connected two nodes before, that remain in the
	 * model.
	 */
	private Stream<SequenceFlow> findRecycledEdges(final VoteContext<String, ? extends BaseElement> voteContext) {
		return voteContext.updated().stream()
				.map(updatedId -> voteContext.fromMap().get(updatedId))
				.filter(SequenceFlow.class::isInstance)
				.map(SequenceFlow.class::cast)
				// check for changed source or target (in toMap)
				.filter(oldEdge -> {
					final var newObj = voteContext.toMap().get(oldEdge.getId());

					if (newObj instanceof final SequenceFlow newEdge) {
						return !oldEdge.getSource().getId().equals(newEdge.getSource().getId())
								|| !oldEdge.getTarget().getId().equals(newEdge.getTarget().getId());
					}

					// mkay, used to be a sequence flow, but isn't anymore
					return false;
				})
				// check if source and target remained in the process
				.filter(removedEdge -> voteContext.toMap().containsKey(removedEdge.getSource().getId())
						&& voteContext.toMap().containsKey(removedEdge.getTarget().getId()));
	}
}
