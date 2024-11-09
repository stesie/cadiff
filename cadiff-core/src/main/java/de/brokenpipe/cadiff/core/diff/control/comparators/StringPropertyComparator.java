package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.cadiff.core.actions.Action;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.function.Function;
import java.util.stream.Stream;

interface StringPropertyComparator<E extends BaseElement> extends PropertyComparator<E, String> {

	default Stream<Action> compareStringProperty(final Function<E, String> accessor,
			final Class<? extends AbstractChangePropertyAction<String>> clazz, final E from, final E to) {
		return compareProperty(accessor, String.class, clazz, from, to);
	}
}
