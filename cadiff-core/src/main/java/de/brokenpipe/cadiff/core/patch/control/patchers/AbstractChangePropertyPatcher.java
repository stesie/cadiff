package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class AbstractChangePropertyPatcher<ElementT extends BaseElement, ValueT> implements Patcher {

	private final AbstractChangePropertyAction<ValueT> action;
	private final Class<ElementT> elementType;
	private final Function<ElementT, ValueT> getter;
	private final BiConsumer<ElementT, ValueT> setter;

	@Override
	public void accept(final PatcherContext context) {
		final ModelElementInstance target = context.getModelInstance().getModelElementById(action.getId());

		if (target == null) {
			throw new TargetElementNotFoundException(action.getId());
		}

		if (!elementType.isInstance(target)) {
			throw new UnexpectedTargetElementTypeException(action.getId(), FlowElement.class.getSimpleName(),
					target.getClass().getSimpleName());
		}

		final ElementT targetElement = elementType.cast(target);

		if (action.getOldValue() == null
				? getter.apply(targetElement) != null
				: !action.getOldValue().equals(getter.apply(targetElement))) {
			throw new ValueMismatchException(action.getId(), getter.apply(targetElement), action.getOldValue());
		}

		setter.accept(targetElement, action.getNewValue());
	}
}
