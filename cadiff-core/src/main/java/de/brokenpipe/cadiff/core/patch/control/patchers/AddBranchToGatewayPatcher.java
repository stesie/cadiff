package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddBranchToGatewayAction;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class AddBranchToGatewayPatcher extends AbstractPatcher implements Patcher {

	private final AddBranchToGatewayAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {

		// steps is a flow like this: SourceNode -> NewEdge -> NewNode -> NewEdge -> NewNode -> NewEdge -> TargetNode
		// both SourceNode & TargetNode already exist, nodes are on even numbers

		// insert new nodes first
		for (int i = 2; i < action.steps().size() - (action.finalElementIsNew() ? 0 : 1); i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);
			addFlowElement(bpmnModelInstance, step.id(), step.elementTypeName(),
					step.bounds().orElseThrow());
		}

		// insert new edges afterward
		for (int i = 1; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);

			addSequenceFlow(bpmnModelInstance, step.id(), action.steps().get(i - 1).id(),
					action.steps().get(i + 1).id(), step.waypoints().orElseThrow());
		}

	}
}
