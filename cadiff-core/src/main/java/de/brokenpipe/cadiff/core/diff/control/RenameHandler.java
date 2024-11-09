package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;
import de.brokenpipe.cadiff.core.diff.control.voters.VoterRegistry;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RenameHandler<T extends BaseElement> {

	private final VoteContext<T> context;

	public List<Action> apply() {
		final List<Action> actions = new ArrayList<>();

		while (true) {
			final Optional<Action> action = findRenameCandidate();

			if (action.isEmpty()) {
				break;
			}

			actions.add(action.get());
		}

		return actions;
	}

	private Optional<Action> findRenameCandidate() {
		for (final String addId : context.added()) {
			final List<VoteResult> results = context.removed().stream()
					.map(removeId -> new VoteResult(removeId, VoterRegistry.INSTANCE.apply(removeId, addId, context)))
					.sorted(Comparator.comparingInt(VoteResult::score).reversed())
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
}
