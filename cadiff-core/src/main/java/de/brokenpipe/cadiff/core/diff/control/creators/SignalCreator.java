package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.AddSignalAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Signal;

import java.util.Optional;

public class SignalCreator implements Creator {
	@Override
	public Integer getPriority() {
		return Integer.valueOf(-1000);
	}

	@Override
	public Optional<AddAction> apply(final VoteContext<String, ? extends BaseElement> voteContext) {
		return voteContext.added().stream()
				.map(addId -> voteContext.toMap().get(addId))
				.filter(Signal.class::isInstance)
				.map(Signal.class::cast)
				.map(signal -> (AddAction) new AddSignalAction(signal.getId()))
				.findFirst();
	}
}
