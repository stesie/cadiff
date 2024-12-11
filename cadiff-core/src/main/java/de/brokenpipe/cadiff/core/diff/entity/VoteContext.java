package de.brokenpipe.cadiff.core.diff.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public record VoteContext<T> (Map<String, T> fromMap, Map<String, T> toMap,
						  Set<String> updated, Set<String> removed, Set<String> added) {

	public static <T> VoteContext<T> partition(final Function<T, String> extractId, final Collection<T> from, final Collection<T> to) {
		final Map<String, T> fromMap = from.stream().collect(Collectors.toMap(extractId, e -> e));
		final Map<String, T> toMap = to.stream().collect(Collectors.toMap(extractId, e -> e));

		final Set<String> updated = new HashSet<>(fromMap.keySet());
		updated.retainAll(toMap.keySet());

		final Set<String> removed = new HashSet<>(fromMap.keySet());
		removed.removeAll(toMap.keySet());

		final Set<String> added = new HashSet<>(toMap.keySet());
		added.removeAll(fromMap.keySet());

		return new VoteContext<>(fromMap, toMap, updated, removed, added);
	}
}
