package de.brokenpipe.cadiff.core.diff.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public record VoteContext<K, T> (Map<K, T> fromMap, Map<K, T> toMap,
						  Set<K> updated, Set<K> removed, Set<K> added) {

	public static <K, T> VoteContext<K, T> partition(final Function<T, K> extractId, final Collection<T> from, final Collection<T> to) {
		final Map<K, T> fromMap = from.stream().collect(Collectors.toMap(extractId, e -> e));
		final Map<K, T> toMap = to.stream().collect(Collectors.toMap(extractId, e -> e));

		final Set<K> updated = new HashSet<>(fromMap.keySet());
		updated.retainAll(toMap.keySet());

		final Set<K> removed = new HashSet<>(fromMap.keySet());
		removed.removeAll(toMap.keySet());

		final Set<K> added = new HashSet<>(toMap.keySet());
		added.removeAll(fromMap.keySet());

		return new VoteContext<>(fromMap, toMap, updated, removed, added);
	}
}
