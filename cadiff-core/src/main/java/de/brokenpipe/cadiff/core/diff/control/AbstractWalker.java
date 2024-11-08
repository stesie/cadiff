package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.DeleteProcessAction;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
abstract class AbstractWalker<T extends BaseElement> {

	private final Collection<T> from;
	private final Collection<T> to;

	public Stream<Action> walk() {
		final Map<String, T> fromMap = from.stream().collect(Collectors.toMap(BaseElement::getId, e -> e));
		final Map<String, T> toMap = to.stream().collect(Collectors.toMap(BaseElement::getId, e -> e));

		final Set<String> updated = new HashSet<>(fromMap.keySet());
		updated.retainAll(toMap.keySet());

		final Set<String> removed = new HashSet<>(fromMap.keySet());
		removed.removeAll(toMap.keySet());

		final Set<String> added = new HashSet<>(toMap.keySet());
		added.removeAll(fromMap.keySet());

		return mergeStreams(List.of(
				updated.stream().flatMap(id -> handleUpdated(fromMap.get(id), toMap.get(id))),
				removed.stream().flatMap(id -> handleRemoved(fromMap.get(id))),
				added.stream().flatMap(id -> handleAdded(toMap.get(id)))));
	}

	protected abstract Stream<Action> handleUpdated(T from, T to);

	protected Stream<Action> handleRemoved(final T removed) {
		return Stream.of(new DeleteProcessAction(removed.getId()));
	}

	protected abstract Stream<Action> handleAdded(T added);

	protected static <T> Stream<T> mergeStreams(final Collection<Stream<T>> streams) {
		return streams.stream().flatMap(x -> x);
	}
}
