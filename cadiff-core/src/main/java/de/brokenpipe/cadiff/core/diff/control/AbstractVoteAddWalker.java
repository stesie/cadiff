package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.StreamUtils.mergeStreams;

/**
 * Abstract class to walk through two collections of elements and create a list of actions to transform the first
 * collection into the second. New elements are handled by trying to detect id changes first through a round of voting,
 * afterward by running the {@link AddHandler}.
 */
@RequiredArgsConstructor
abstract class AbstractVoteAddWalker<T extends BaseElement> {

	protected final CompareContext<Collection<T>> compareContext;

	public Stream<Action> walk() {

		final VoteContext<String, T> voteContext = VoteContext.partition(BaseElement::getId, compareContext.from(), compareContext.to()); // partitionElements();

		return mergeStreams(List.of(
				new RenameHandler<>(voteContext).apply().stream(),
				new AddHandler<>(compareContext, voteContext).apply().stream(),
				voteContext.updated().stream()
						.flatMap(id -> handleUpdated(
								new CompareContext<>(compareContext.fromInstance(), compareContext.fromContainer(), voteContext.fromMap().get(id), voteContext.toMap().get(id))
								)),
				voteContext.removed().stream().flatMap(id -> handleRemoved(voteContext.fromMap().get(id)))));
	}

	protected abstract Stream<Action> handleUpdated(final CompareContext<T> context);

	protected Stream<Action> handleRemoved(final T removed) {
		return Stream.of(new DeleteElementAction(removed.getId()));
	}

}
