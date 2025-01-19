package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddFlowAction;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;

@RequiredArgsConstructor
public class AddFlowPatcher extends AbstractPatcher implements Patcher {

	private final AddFlowAction action;

	@Override
	public void accept(final PatcherContext context) {

		// steps is a flow like this: BoundaryEvent -> NewEdge -> NewNode -> NewEdge -> NewNode -> NewEdge -> TargetNode
		// BoundaryEvent is new, TargetNode potentially exists, nodes are on even numbers

		// insert new nodes first
		for (int i = 0; i < action.steps().size() - (action.finalElementIsNew() ? 0 : 1); i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);
			final BaseElement addedElement = addFlowElement(context, step.id(), step.elementTypeName(),
					step.bounds().orElseThrow());

			if (i == 0 && action.attachedToId().isPresent() && addedElement instanceof final BoundaryEvent boundaryEvent) {
				final Activity attachedTo = findTargetWithType(context, action.attachedToId().get(), Activity.class);
				boundaryEvent.setAttachedTo(attachedTo);
			}
		}

		// insert new edges afterward
		for (int i = 1; i < action.steps().size() - 1; i += 2) {
			final InsertNodeOnEdgeAction.Step step = action.steps().get(i);

			addSequenceFlow(context, step.id(), action.steps().get(i - 1).id(),
					action.steps().get(i + 1).id(), step.waypoints().orElseThrow());
		}

	}
}
