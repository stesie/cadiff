package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class InsertNodeOnEdgePatcher extends AbstractPatcher implements Patcher {

	private final InsertNodeOnEdgeAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		action.getIdsRemoved().forEach(id -> deleteElement(bpmnModelInstance, id));

		// steps is a flow like this: SourceNode -> NewEdge -> NewNode -> NewEdge -> NewNode -> NewEdge -> TargetNode
		// both SourceNode & TargetNode already exist, nodes are on even numbers

		// insert new nodes first
		for (int i = 2; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);
			addFlowElement(bpmnModelInstance, step.id(), step.elementTypeName(),
					step.bounds().orElseThrow());
		}

		// insert new edges afterward
		for (int i = 1; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);

			if (step.id().equals(action.replaceFlowId())) {
				// FIXME we must update the source/target of the edge. And maybe also the waypoints !?
				continue;
			}

			addSequenceFlow(bpmnModelInstance, step.id(), action.steps().get(i - 1).id(),
					action.steps().get(i + 1).id(), step.waypoints().orElseThrow());
		}

	}
}
