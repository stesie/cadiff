package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeSignalEventDefinitionAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.BPMN_ATTRIBUTE_SIGNAL_REF;

@RequiredArgsConstructor
public class ChangeSignalEventDefinitionPatcher extends AbstractPatcher implements Patcher {

	private final ChangeSignalEventDefinitionAction action;

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

		final SignalEventDefinition signalDef = eventDefinitions.stream()
				.filter(x -> x.getId().equals(action.signalDefinitionId()))
				.findFirst()
				.map(eventDefinition -> {
					if (eventDefinition instanceof final SignalEventDefinition signalEventDefinition) {
						return signalEventDefinition;
					}

					throw new UnexpectedTargetElementTypeException(action.signalDefinitionId(),
							target.getClass().getSimpleName(),
							SignalEventDefinition.class.getSimpleName());
				})
				.orElseGet(() -> {
					final var ed = context.getModelInstance()
							.newInstance(SignalEventDefinition.class, action.signalDefinitionId());
					target.addChildElement(ed);
					return ed;
				});

		final String actualOldValue = Optional.ofNullable(signalDef.getSignal()).map(BaseElement::getId).orElse(null);

		if (actualOldValue == null
				? action.oldSignalRef() != null
				: !actualOldValue.equals(action.oldSignalRef())) {
			throw new ValueMismatchException(action.id() + "/" + action.signalDefinitionId(), actualOldValue,
					action.oldSignalRef());
		}

		if (action.newSignalRef() == null) {
			signalDef.removeAttribute(BPMN_ATTRIBUTE_SIGNAL_REF);
			return;
		}

		final Signal refSignal = findTargetWithType(context, action.newSignalRef(), Signal.class);
		signalDef.setSignal(refSignal);
	}
}
