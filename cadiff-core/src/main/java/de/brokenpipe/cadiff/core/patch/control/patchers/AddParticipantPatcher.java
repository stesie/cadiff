package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddParticipantAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Collaboration;
import org.camunda.bpm.model.bpmn.instance.Participant;

import java.util.Optional;

@RequiredArgsConstructor
public class AddParticipantPatcher extends AbstractPatcher implements Patcher {

	private final AddParticipantAction action;

	@Override
	public void accept(final PatcherContext context) {
		final BaseElement addedElement = context.getModelInstance().newInstance(Participant.class, action.id());

		final Optional<Collaboration> collaboration =
				findRootElementByType(context.getModelInstance(), Collaboration.class);

		if (collaboration.isEmpty()) {
			throw new TargetElementNotFoundException("Collaboration not found");
		}

		collaboration.get().addChildElement(addedElement);
	}

}
