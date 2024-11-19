package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.diff.control.creators.CreatorRegistry;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
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
		final T randomFromElement = context.fromMap().values().iterator().next();
		final var fromInstance = (BpmnModelInstance) randomFromElement.getModelInstance();

		final List<Action> actions = new ArrayList<>();

		while (true) {
			final Optional<AddAction> candidate = findCandidate();

			if (candidate.isEmpty()) {
				break;
			}

			final AddAction action = candidate.get();

			// add/replace elements to from instance
			action.patcher().accept(PatcherContext.of(fromInstance, (BaseElement) randomFromElement.getParentElement())); // FIXME do we need the process here !?

			action.idsAdded().forEach(id -> {
				context.added().remove(id);
				context.updated().add(id);
				context.fromMap().put(id, fromInstance.getModelElementById(id));
			});

			action.idsRemoved().forEach(id -> {
				context.removed().remove(id);
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
