package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.StreamUtils.mergeStreams;

/**
 * Simple walker that compares both collections and runs add/update/remove handlers.
 * @param <T>
 */
@RequiredArgsConstructor
public abstract class AbstractSimpleWalker<K, T> {

	private final Collection<T> from;
	private final Collection<T> to;

	public Stream<Action> walk() {

		final VoteContext<K, T> context = VoteContext.partition(this::extractKey, from, to);

		return mergeStreams(List.of(
				context.added().stream()
						.flatMap(id -> handleAdded(context.toMap().get(id))),
				context.updated().stream()
						.flatMap(id -> handleUpdated(context.fromMap().get(id), context.toMap().get(id))),
				context.removed().stream().flatMap(id -> handleRemoved(context.fromMap().get(id)))));
	}

	protected abstract K extractKey(final T element);

	protected abstract Stream<Action> handleAdded(final T added);

	protected abstract Stream<Action> handleUpdated(T from, T to);

	protected abstract Stream<Action> handleRemoved(final T removed);

}
