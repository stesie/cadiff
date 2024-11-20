package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Abstract class to walk through two collections of elements and create a list of actions to transform the first
 * collection into the second. New elements are handled by trying to detect id changes first through a round of voting,
 * afterward by running the {@link AddHandler}.
 */
abstract class AbstractVoteAddWalker<T extends BaseElement> extends AbstractSimpleWalker<T> {

	public AbstractVoteAddWalker(final Collection<T> from, final Collection<T> to) {
		super(from, to);
	}

	@Override
	protected String extractId(final T element) {
		return element.getId();
	}

	@Override
	public Stream<Action> walk() {

		final VoteContext<T> context = partitionElements();

		return mergeStreams(List.of(
				new RenameHandler<>(context).apply().stream(),
				new AddHandler<>(context).apply().stream(),
				context.updated().stream()
						.flatMap(id -> handleUpdated(context.fromMap().get(id), context.toMap().get(id))),
				context.removed().stream().flatMap(id -> handleRemoved(context.fromMap().get(id)))));
	}

	@Override
	protected Stream<Action> handleAdded(final T added) {
		throw new IllegalStateException("This method should not be called");
	}

	@Override
	protected Stream<Action> handleRemoved(final T removed) {
		return Stream.of(new DeleteElementAction(removed.getId()));
	}

}
