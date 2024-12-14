package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddCollaborationAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Collaboration;

@RequiredArgsConstructor
public class AddCollaborationPatcher extends AbstractPatcher implements Patcher {

	private final AddCollaborationAction action;

	@Override
	public void accept(final PatcherContext context) {
		final BaseElement addedElement = context.getModelInstance().newInstance(Collaboration.class, action.id());
		context.getModelInstance().getDefinitions().addChildElement(addedElement);
	}

}
