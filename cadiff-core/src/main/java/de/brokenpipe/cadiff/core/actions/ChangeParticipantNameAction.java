package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericChangePropertyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.Participant;

public record ChangeParticipantNameAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new GenericChangePropertyPatcher<>(this, Participant.class,
				Participant::getName, Participant::setName);
	}
}
