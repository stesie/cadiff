package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddParticipantAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Participant;

import java.util.Optional;

public class ParticipantCreator implements Creator {
	@Override
	public Integer getPriority() {
		return Integer.valueOf(-1000);
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<? extends BaseElement> voteContext) {
		return voteContext.added().stream()
				.map(addId -> voteContext.toMap().get(addId))
				.filter(Participant.class::isInstance)
				.map(Participant.class::cast)
				.map(error -> (AddAction) new AddParticipantAction(error.getId()))
				.findFirst();
	}
}
