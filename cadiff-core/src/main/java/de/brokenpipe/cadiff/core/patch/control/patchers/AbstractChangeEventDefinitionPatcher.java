package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.CatchEvent;
import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.ThrowEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractChangeEventDefinitionPatcher<ED extends EventDefinition> extends AbstractPatcher implements Patcher {

	protected final String id;
	protected final String definitionId;
	protected final Class<ED> edClass;

	protected abstract void applyChange(final PatcherContext context, final ED def);

	@Override
	public void accept(final PatcherContext context) {
		final ModelElementInstance target = context.getModelInstance().getModelElementById(id);

		switch (target) {
		case null -> throw new TargetElementNotFoundException(id);

		case final ThrowEvent throwEvent ->
				updateEventDefinition(context, target, throwEvent.getEventDefinitions());

		case final CatchEvent catchEvent ->
				updateEventDefinition(context, target, catchEvent.getEventDefinitions());

		default -> throw new UnexpectedTargetElementTypeException(id, target.getClass().getSimpleName(),
				ThrowEvent.class.getSimpleName());
		}
	}

	private void updateEventDefinition(final PatcherContext context, final ModelElementInstance target,
			final Collection<EventDefinition> eventDefinitions) {

		final ED def = eventDefinitions.stream()
				.filter(x -> Objects.equals(definitionId, x.getId()))
				.findFirst()
				.map(eventDefinition -> {
					if (edClass.isInstance(eventDefinition)) {
						return edClass.cast(eventDefinition);
					}

					throw new UnexpectedTargetElementTypeException(definitionId,
							target.getClass().getSimpleName(),
							edClass.getSimpleName());
				})
				.orElseGet(() -> {
					final var ed = context.getModelInstance()
							.newInstance(edClass, definitionId);
					target.addChildElement(ed);
					return ed;
				});

		applyChange(context, def);
	}
}
