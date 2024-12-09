package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple walker that compares both collections and runs add/update/remove handlers.
 * @param <T>
 */
@RequiredArgsConstructor
public abstract class AbstractSimpleWalker<T> {

	private final Collection<T> from;
	private final Collection<T> to;

	public Stream<Action> walk() {

		final VoteContext<T> context = partitionElements();

		return mergeStreams(List.of(
				context.added().stream()
						.flatMap(id -> handleAdded(context.toMap().get(id))),
				context.updated().stream()
						.flatMap(id -> handleUpdated(context.fromMap().get(id), context.toMap().get(id))),
				context.removed().stream().flatMap(id -> handleRemoved(context.fromMap().get(id)))));
	}

	protected VoteContext<T> partitionElements() {
		final Map<String, T> fromMap = from.stream().collect(Collectors.toMap(this::extractId, e -> e));
		final Map<String, T> toMap = to.stream().collect(Collectors.toMap(this::extractId, e -> e));

		final Set<String> updated = new HashSet<>(fromMap.keySet());
		updated.retainAll(toMap.keySet());

		final Set<String> removed = new HashSet<>(fromMap.keySet());
		removed.removeAll(toMap.keySet());

		final Set<String> added = new HashSet<>(toMap.keySet());
		added.removeAll(fromMap.keySet());

		return new VoteContext<>(fromMap, toMap, updated, removed, added);
	}

	protected abstract String extractId(final T element);

	protected abstract Stream<Action> handleAdded(final T added);

	protected abstract Stream<Action> handleUpdated(T from, T to);

	protected abstract Stream<Action> handleRemoved(final T removed);

	protected static <T> Stream<T> mergeStreams(final Collection<Stream<T>> streams) {
		return streams.stream().flatMap(x -> x);
	}
}
