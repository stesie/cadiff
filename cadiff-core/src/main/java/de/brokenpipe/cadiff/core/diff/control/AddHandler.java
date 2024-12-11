package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.diff.control.creators.CreatorRegistry;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AddHandler<T extends BaseElement> {

	protected final CompareContext<Collection<T>> compareContext;
	private final VoteContext<T> voteContext;

	public List<Action> apply() {
		final List<Action> actions = new ArrayList<>();

		while (true) {
			final Optional<AddAction> candidate = findCandidate();

			if (candidate.isEmpty()) {
				break;
			}

			final AddAction action = candidate.get();

			// add/replace elements to from instance
			action.patcher().accept(PatcherContext.of(compareContext.fromInstance(), compareContext.fromContainer()));

			action.idsAdded().forEach(id -> {
				voteContext.added().remove(id);
				voteContext.updated().add(id);
				voteContext.fromMap().put(id, compareContext.fromInstance().getModelElementById(id));
			});

			action.idsRemoved().forEach(id -> {
				voteContext.removed().remove(id);
			});

			actions.add(action);
		}

		if (!voteContext.added().isEmpty()) {
			throw new IllegalStateException("Not all added elements could be handled");
		}

		return actions;
	}

	private Optional<AddAction> findCandidate() {
		return CreatorRegistry.INSTANCE.apply(voteContext);
	}

}
