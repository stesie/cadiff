package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeAttachedToAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeAttachedToPatcher implements Patcher {

	private final ChangeAttachedToAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		final ModelElementInstance target = bpmnModelInstance.getModelElementById(action.getId());

		if (target == null) {
			throw new TargetElementNotFoundException(action.getId());
		}

		if (target instanceof final BoundaryEvent boundaryEvent) {
			final var actualOldValue = Optional.ofNullable(boundaryEvent.getAttachedTo())
					.map(FlowElement::getId)
					.orElse(null);

			if (actualOldValue == null
					? action.getOldValue() != null
					: !actualOldValue.equals(action.getOldValue())) {
				throw new ValueMismatchException(action.getId(), actualOldValue, action.getOldValue());
			}

			if (action.getNewValue() == null) {
				boundaryEvent.setAttachedTo(null);
				return;
			}

			final var attachedTo = bpmnModelInstance.getModelElementById(action.getNewValue());

			if (attachedTo == null) {
				throw new TargetElementNotFoundException(action.getNewValue());
			}

			if (attachedTo instanceof final Activity attachedToActivity) {
				boundaryEvent.setAttachedTo(attachedToActivity);
				return;
			}

			throw new UnexpectedTargetElementTypeException(action.getNewValue(), target.getClass().getSimpleName(),
					Activity.class.getSimpleName());
		}

		throw new UnexpectedTargetElementTypeException(action.getId(), target.getClass().getSimpleName(),
				BoundaryEvent.class.getSimpleName());
	}
}
