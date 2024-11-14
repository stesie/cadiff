package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeErrorEventDefinitionAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Error;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeErrorEventDefinitionPatcher implements Patcher {

	private final ChangeErrorEventDefinitionAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		final ModelElementInstance target = bpmnModelInstance.getModelElementById(action.id());

		switch (target) {
		case null -> throw new TargetElementNotFoundException(action.id());

		case final ThrowEvent throwEvent ->
				updateEventDefinition(bpmnModelInstance, target, throwEvent.getEventDefinitions());

		case final CatchEvent catchEvent ->
				updateEventDefinition(bpmnModelInstance, target, catchEvent.getEventDefinitions());

		default -> throw new UnexpectedTargetElementTypeException(action.id(), target.getClass().getSimpleName(),
				ThrowEvent.class.getSimpleName());
		}
	}

	private void updateEventDefinition(final BpmnModelInstance bpmnModelInstance, final ModelElementInstance target,
			final Collection<EventDefinition> eventDefinitions) {

		final Optional<ErrorEventDefinition> existingErrorDef = eventDefinitions.stream()
				.filter(x -> x.getId().equals(action.errorDefinitionId()))
				.findFirst()
				.map(eventDefinition -> {
					if (eventDefinition instanceof final ErrorEventDefinition errorEventDefinition) {
						return errorEventDefinition;
					}

					throw new UnexpectedTargetElementTypeException(action.errorDefinitionId(),
							target.getClass().getSimpleName(),
							ErrorEventDefinition.class.getSimpleName());
				});

		final String actualOldValue = existingErrorDef.map(x -> x.getError().getId()).orElse(null);

		if (actualOldValue == null
				? action.oldErrorRef() != null
				: !actualOldValue.equals(action.oldErrorRef())) {
			throw new ValueMismatchException(action.id() + "/" + action.errorDefinitionId(), actualOldValue,
					action.oldErrorRef());
		}

		if (action.newErrorRef() == null) {
			existingErrorDef.ifPresent(ed -> ed.getParentElement().removeChildElement(ed));
			return;
		}

		final var ref = bpmnModelInstance.getModelElementById(action.newErrorRef());

		if (ref == null) {
			throw new TargetElementNotFoundException(action.newErrorRef());
		}

		if (ref instanceof final Error refError) {
			if (existingErrorDef.isPresent()) {
				existingErrorDef.get().setError(refError);
			} else {
				final ErrorEventDefinition ed = bpmnModelInstance.newInstance(ErrorEventDefinition.class,
						action.errorDefinitionId());
				ed.setError(refError);
				target.addChildElement(ed);
			}

			return;
		}

		throw new UnexpectedTargetElementTypeException(action.newErrorRef(), target.getClass().getSimpleName(),
				Error.class.getSimpleName());
	}
}
