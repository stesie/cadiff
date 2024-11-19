package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeAttachedToAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeAttachedToPatcher implements Patcher {

	private final ChangeAttachedToAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ModelElementInstance target = context.getModelInstance().getModelElementById(action.id());

		if (target == null) {
			throw new TargetElementNotFoundException(action.id());
		}

		if (target instanceof final BoundaryEvent boundaryEvent) {
			final var actualOldValue = Optional.ofNullable(boundaryEvent.getAttachedTo())
					.map(FlowElement::getId)
					.orElse(null);

			if (actualOldValue == null
					? action.oldValue() != null
					: !actualOldValue.equals(action.oldValue())) {
				throw new ValueMismatchException(action.id(), actualOldValue, action.oldValue());
			}

			if (action.newValue() == null) {
				boundaryEvent.setAttachedTo(null);
				return;
			}

			final var attachedTo = context.getModelInstance().getModelElementById(action.newValue());

			if (attachedTo == null) {
				throw new TargetElementNotFoundException(action.newValue());
			}

			if (attachedTo instanceof final Activity attachedToActivity) {
				boundaryEvent.setAttachedTo(attachedToActivity);
				return;
			}

			throw new UnexpectedTargetElementTypeException(action.newValue(), target.getClass().getSimpleName(),
					Activity.class.getSimpleName());
		}

		throw new UnexpectedTargetElementTypeException(action.id(), target.getClass().getSimpleName(),
				BoundaryEvent.class.getSimpleName());
	}
}
