package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.control.comparators.SignalNameComparator;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.Signal;

import java.util.Collection;
import java.util.stream.Stream;

public class SignalWalker extends AbstractVoteAddWalker<Signal> {

	public SignalWalker(final CompareContext<Collection<Signal>> compareContext) {
		super(compareContext);
	}

	@Override
	protected Stream<Action> handleUpdated(final CompareContext<Signal> updateContext) {
		return handleNameChange(updateContext);
	}

	@Override
	protected Stream<Action> handleRemoved(final Signal removed) {
		final var action =  super.handleRemoved(removed).findFirst().orElseThrow();

		// remove the signal from the definition, so the effect cascades to the processes
		action.patcher().accept(PatcherContext.of(compareContext.fromInstance(), compareContext.fromContainer()));

		return Stream.of(action);
	}

	private Stream<Action> handleNameChange(final CompareContext<Signal> updateContext) {
		return new SignalNameComparator().apply(updateContext);
	}

}