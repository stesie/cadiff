package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddBoundaryEventBranchAction;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class AddBoundaryEventBranchPatcher extends AbstractPatcher implements Patcher {

	private final AddBoundaryEventBranchAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {

		// steps is a flow like this: BoundaryEvent -> NewEdge -> NewNode -> NewEdge -> NewNode -> NewEdge -> TargetNode
		// BoundaryEvent is new, TargetNode potentially exists, nodes are on even numbers

		// insert new nodes first
		for (int i = 0; i < action.steps().size() - (action.finalElementIsNew() ? 0 : 1); i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);
			addFlowElement(bpmnModelInstance, step.id(), step.elementTypeName(),
					step.bounds().orElseThrow());

			// TODO attach boundaryEvent to underlying node
		}

		// insert new edges afterward
		for (int i = 1; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);

			addSequenceFlow(bpmnModelInstance, step.id(), action.steps().get(i - 1).id(),
					action.steps().get(i + 1).id(), step.waypoints().orElseThrow());
		}

	}
}
