package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.EventDefinition;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ChangeEventDefinitionPatcher<ED extends EventDefinition, T extends BaseElement>
	extends AbstractChangeEventDefinitionPatcher<ED> {

	private final String oldValue;
	private final String newValue;

	private final Class<T> tClass;
	private final Function<ED, T> getter;
	private final BiConsumer<ED, T> setter;
	private final String attributeName;

	public ChangeEventDefinitionPatcher(final String id, final String definitionId, final String oldValue,
			final String newValue, final Class<ED> edClass, final Class<T> tClass, final Function<ED, T> getter,
			final BiConsumer<ED, T> setter, final String attributeName) {
		super(id, definitionId, edClass);

		this.oldValue = oldValue;
		this.newValue = newValue;
		this.tClass = tClass;
		this.getter = getter;
		this.setter = setter;
		this.attributeName = attributeName;
	}

	@Override
	protected void applyChange(final PatcherContext context, final ED def) {
		final String actualOldValue = Optional.ofNullable(getter.apply(def)).map(BaseElement::getId).orElse(null);

		if (!Objects.equals(actualOldValue, oldValue)) {
			throw new ValueMismatchException(id + "/" + definitionId, actualOldValue, oldValue);
		}

		if (newValue == null) {
			def.removeAttribute(attributeName);
			return;
		}

		final T ref = findTargetWithType(context, newValue, tClass);
		setter.accept(def, ref);
	}
}
