package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeParticipantProcessAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeParticipantProcessPatcher extends AbstractPatcher implements Patcher {

	private final ChangeParticipantProcessAction action;

	@Override
	public void accept(final PatcherContext context) {
		final Participant participant = findTargetWithType(context, action.id(), Participant.class);

		final String actualOldValue = Optional.ofNullable(participant.getProcess())
				.map(BaseElement::getId)
				.orElse(null);

		if (!Objects.equals(action.oldValue(), actualOldValue)) {
			throw new ValueMismatchException(action.id(), actualOldValue, action.oldValue());
		}

		final var target = findTargetWithType(context, action.newValue(), Process.class);
		participant.setProcess(target);
	}
}
