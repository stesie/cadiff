package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.diff.control.voters.VoterRegistry;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
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

		final VoteContext<T> context = partitionElements();

		return mergeStreams(List.of(
				handleRenames(context).stream(),
				context.updated().stream()
						.flatMap(id -> handleUpdated(context.fromMap().get(id), context.toMap().get(id))),
				context.removed().stream().flatMap(id -> handleRemoved(context.fromMap().get(id))),
				context.added().stream().flatMap(id -> handleAdded(context.toMap().get(id)))));
	}

	private List<Action> handleRenames(final VoteContext<T> context) {
		final List<Action> actions = new ArrayList<>();

		while (true) {
			final Optional<Action> action = findRenameCandidate(context);

			if (action.isEmpty()) {
				break;
			}

			actions.add(action.get());
		}

		return actions;
	}

	private Optional<Action> findRenameCandidate(final VoteContext<T> context) {
		for (final String addId : context.added()) {
			final List<VoteResult> results = context.removed().stream()
					.map(removeId -> new VoteResult(removeId, VoterRegistry.INSTANCE.apply(removeId, addId, context)))
					.sorted(Comparator.comparingInt(VoteResult::score))
					.toList();

			if (results.isEmpty()) {
				continue;
			}

			if (results.size() >= 2) {
				final VoteResult first = results.get(0);
				final VoteResult second = results.get(1);

				if (first.score() == second.score()) {
					continue;
				}
			}

			if (results.getFirst().score() <= 0) {
				continue;
			}

			final String removeId = results.getFirst().id();

			context.fromMap().get(removeId).setId(addId);
			context.fromMap().put(addId, context.fromMap().get(removeId));
			context.fromMap().remove(removeId);

			context.added().remove(addId);
			context.updated().add(addId);
			context.removed().remove(removeId);

			return Optional.of(new ChangeIdAction(removeId, addId));
		}

		return Optional.empty();
	}

	private record VoteResult(String id, int score) {
	}

	private VoteContext<T> partitionElements() {
		final Map<String, T> fromMap = from.stream().collect(Collectors.toMap(BaseElement::getId, e -> e));
		final Map<String, T> toMap = to.stream().collect(Collectors.toMap(BaseElement::getId, e -> e));

		final Set<String> updated = new HashSet<>(fromMap.keySet());
		updated.retainAll(toMap.keySet());

		final Set<String> removed = new HashSet<>(fromMap.keySet());
		removed.removeAll(toMap.keySet());

		final Set<String> added = new HashSet<>(toMap.keySet());
		added.removeAll(fromMap.keySet());

		return new VoteContext<>(fromMap, toMap, updated, removed, added);
	}

	protected abstract Stream<Action> handleUpdated(T from, T to);

	protected Stream<Action> handleRemoved(final T removed) {
		return Stream.of(new DeleteElementAction(removed.getId()));
	}

	protected abstract Stream<Action> handleAdded(T added);

	protected static <T> Stream<T> mergeStreams(final Collection<Stream<T>> streams) {
		return streams.stream().flatMap(x -> x);
	}
}
