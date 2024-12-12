package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class GenericChangePropertyPatcher<ElementT extends BaseElement, ValueT>
		extends AbstractPatcher implements Patcher {

	private final ChangePropertyAction<ValueT> action;
	private final Class<ElementT> elementType;
	private final Function<ElementT, ValueT> getter;
	private final BiConsumer<ElementT, ValueT> setter;

	@Override
	public void accept(final PatcherContext context) {
		final ElementT targetElement = findTargetWithType(context, action.id(), elementType);

		if (action.oldValue() == null
				? getter.apply(targetElement) != null
				: !action.oldValue().equals(getter.apply(targetElement))) {
			throw new ValueMismatchException(action.id(), getter.apply(targetElement), action.oldValue());
		}

		setter.accept(targetElement, action.newValue());
	}
}
