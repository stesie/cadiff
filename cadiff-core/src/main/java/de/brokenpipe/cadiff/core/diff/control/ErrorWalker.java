package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.control.comparators.ErrorNameComparator;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.Error;

import java.util.Collection;
import java.util.stream.Stream;

public class ErrorWalker extends AbstractVoteAddWalker<Error> {

	public ErrorWalker(final CompareContext<Collection<Error>> compareContext) {
		super(compareContext);
	}

	@Override
	protected Stream<Action> handleUpdated(final CompareContext<Error> updateContext) {
		return handleNameChange(updateContext);
	}

	@Override
	protected Stream<Action> handleRemoved(final Error removed) {
		final var action =  super.handleRemoved(removed).findFirst().orElseThrow();

		// remove the error from the definition, so the effect cascades to the processes
		action.patcher().accept(PatcherContext.of(compareContext.fromInstance(), compareContext.fromContainer()));

		return Stream.of(action);
	}

	private Stream<Action> handleNameChange(final CompareContext<Error> updateContext) {
		return new ErrorNameComparator().apply(updateContext);
	}

}