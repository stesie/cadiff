package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.control.comparators.ComparatorRegistry;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.Participant;

import java.util.Collection;
import java.util.stream.Stream;

public class ParticipantWalker extends AbstractVoteAddWalker<Participant> {

	public ParticipantWalker(final CompareContext<Collection<Participant>> compareContext) {
		super(compareContext);
	}

	@Override
	protected Stream<Action> handleUpdated(final CompareContext<Participant> context) {
		return ComparatorRegistry.INSTANCE.apply(context);
	}
}
