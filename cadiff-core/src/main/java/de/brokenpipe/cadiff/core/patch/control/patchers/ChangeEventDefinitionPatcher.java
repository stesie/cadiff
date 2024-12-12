package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.CatchEvent;
import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.ThrowEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class ChangeEventDefinitionPatcher<AT extends ChangePropertyAction<String>, ED extends EventDefinition, T extends BaseElement> extends AbstractPatcher implements Patcher {

	private final AT action;
	private final Class<ED> edClass;
	private final Class<T> tClass;
	private final Function<AT, String> definitionIdReader;
	private final Function<ED, T> getter;
	private final BiConsumer<ED, T> setter;
	private final String attributeName;

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

		final ED def = eventDefinitions.stream()
				.filter(x -> x.getId().equals(definitionIdReader.apply(action)))
				.findFirst()
				.map(eventDefinition -> {
					if (edClass.isInstance(eventDefinition)) {
						return edClass.cast(eventDefinition);
					}

					throw new UnexpectedTargetElementTypeException(definitionIdReader.apply(action),
							target.getClass().getSimpleName(),
							edClass.getSimpleName());
				})
				.orElseGet(() -> {
					final var ed = context.getModelInstance()
							.newInstance(edClass, definitionIdReader.apply(action));
					target.addChildElement(ed);
					return ed;
				});

		final String actualOldValue = Optional.ofNullable(getter.apply(def)).map(BaseElement::getId).orElse(null);

		if (actualOldValue == null
				? action.oldValue() != null
				: !actualOldValue.equals(action.oldValue())) {
			throw new ValueMismatchException(action.id() + "/" + definitionIdReader.apply(action), actualOldValue,
					action.oldValue());
		}

		if (action.newValue() == null) {
			def.removeAttribute(attributeName);
			return;
		}

		final T ref = findTargetWithType(context, action.newValue(), tClass);
		setter.accept(def, ref);
	}
}
