package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.function.Function;
import java.util.stream.Stream;

public interface StringPropertyComparator<E extends BaseElement> extends PropertyComparator<E, String> {

	default Stream<Action> compareStringProperty(final Function<E, String> accessor,
			final Class<? extends ChangePropertyAction<String>> clazz, final CompareContext<? extends E> compareContext) {
		return compareProperty(accessor, String.class, clazz, compareContext);
	}
}
