package de.brokenpipe.cadiff.core.diff.entity;

import java.util.Map;
import java.util.Set;

public record VoteContext<T> (Map<String, T> fromMap, Map<String, T> toMap,
						  Set<String> updated, Set<String> removed, Set<String> added) {
}
