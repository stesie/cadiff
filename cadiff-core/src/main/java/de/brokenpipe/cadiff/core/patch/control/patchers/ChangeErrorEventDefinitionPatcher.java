package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeErrorEventDefinitionAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Error;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeErrorEventDefinitionPatcher extends AbstractPatcher implements Patcher {

	private final ChangeErrorEventDefinitionAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ModelElementInstance target = context.getModelInstance().getModelElementById(action.id());

		switch (target) {
		case null -> throw new TargetElementNotFoundException(action.id());

		case final ThrowEvent throwEvent ->
				updateEventDefinition(context, target, throwEvent.getEventDefinitions());

		case final CatchEvent catchEvent ->
				updateEventDefinition(context, target, catchEvent.getEventDefinitions());

		default -> throw new UnexpectedTargetElementTypeException(action.id(), target.getClass().getSimpleName(),
				ThrowEvent.class.getSimpleName());
		}
	}

	private void updateEventDefinition(final PatcherContext context, final ModelElementInstance target,
			final Collection<EventDefinition> eventDefinitions) {

		final ErrorEventDefinition errorDef = eventDefinitions.stream()
				.filter(x -> x.getId().equals(action.errorDefinitionId()))
				.findFirst()
				.map(eventDefinition -> {
					if (eventDefinition instanceof final ErrorEventDefinition errorEventDefinition) {
						return errorEventDefinition;
					}

					throw new UnexpectedTargetElementTypeException(action.errorDefinitionId(),
							target.getClass().getSimpleName(),
							ErrorEventDefinition.class.getSimpleName());
				})
				.orElseGet(() -> {
					final var ed = context.getModelInstance()
							.newInstance(ErrorEventDefinition.class, action.errorDefinitionId());
					target.addChildElement(ed);
					return ed;
				});

		final String actualOldValue = Optional.ofNullable(errorDef.getError()).map(BaseElement::getId).orElse(null);

		if (actualOldValue == null
				? action.oldErrorRef() != null
				: !actualOldValue.equals(action.oldErrorRef())) {
			throw new ValueMismatchException(action.id() + "/" + action.errorDefinitionId(), actualOldValue,
					action.oldErrorRef());
		}

		if (action.newErrorRef() == null) {
			// FIXME what shall newErrorRef mean? Remove the whole error definition? or just the errorRef?
			// errorDef.removeAttribute(BPMN_ATTRIBUTE_ERROR_REF);
			errorDef.getParentElement().removeChildElement(errorDef);
			return;
		}

		final Error refError = findTargetWithType(context, action.newErrorRef(), Error.class);
		errorDef.setError(refError);
	}
}
