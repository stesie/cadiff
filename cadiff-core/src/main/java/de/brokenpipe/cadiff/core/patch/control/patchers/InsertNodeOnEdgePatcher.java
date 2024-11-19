package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.List;

@RequiredArgsConstructor
public class InsertNodeOnEdgePatcher extends AbstractPatcher implements Patcher {

	private final InsertNodeOnEdgeAction action;

	@Override
	public void accept(final PatcherContext context) {
		action.idsRemoved().forEach(id -> deleteElement(context.getModelInstance(), id));

		// steps is a flow like this: SourceNode -> NewEdge -> NewNode -> NewEdge -> NewNode -> NewEdge -> TargetNode
		// both SourceNode & TargetNode already exist, nodes are on even numbers

		// insert new nodes first
		for (int i = 2; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);
			addFlowElement(context, step.id(), step.elementTypeName(),
					step.bounds().orElseThrow());
		}

		// insert new edges afterward
		for (int i = 1; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);

			if (step.id().equals(action.replaceFlowId())) {
				updateSequenceFlow(context, action.replaceFlowId(), action.steps().get(i - 1).id(),
						action.steps().get(i + 1).id(), step.waypoints().orElseThrow());
				continue;
			}

			addSequenceFlow(context, step.id(), action.steps().get(i - 1).id(),
					action.steps().get(i + 1).id(), step.waypoints().orElseThrow());
		}

	}

	private void updateSequenceFlow(final PatcherContext context, final String edgeId,
			final String fromId, final String toId, final List<Waypoint> waypoints) {

		updateSequenceFlow(context, findTargetWithType(context, edgeId, SequenceFlow.class), fromId, toId);

		// FIXME update waypoints
	}
}
