package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.diff.control.creators.CreatorRegistry;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AddHandler<T extends BaseElement> {

	private final VoteContext<T> context;

	public List<Action> apply() {
		// TODO add instance to context !?
		final var fromInstance = (BpmnModelInstance) context.fromMap().values().iterator().next().getModelInstance();

		final List<Action> actions = new ArrayList<>();

		while (true) {
			final Optional<AddAction> candidate = findCandidate();

			if (candidate.isEmpty()) {
				break;
			}

			final AddAction action = candidate.get();

			// add elements to from instance
			action.getPatcher().accept(fromInstance);

			action.getIds().forEach(id -> {
				context.added().remove(id);
				context.updated().add(id);
				context.fromMap().put(id, fromInstance.getModelElementById(id));
			});

			actions.add(action);
		}

		if (!context.added().isEmpty()) {
			throw new IllegalStateException("Not all added elements could be handled");
		}

		return actions;
	}

	private Optional<AddAction> findCandidate() {
		return CreatorRegistry.INSTANCE.apply(context);
	}

}
