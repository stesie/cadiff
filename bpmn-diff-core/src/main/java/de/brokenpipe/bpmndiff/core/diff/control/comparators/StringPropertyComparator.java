package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.bpmndiff.core.actions.Action;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.function.Function;
import java.util.stream.Stream;

interface StringPropertyComparator<E extends FlowElement> extends PropertyComparator<E, String> {

	default Stream<Action> compareStringProperty(final Function<E, String> accessor,
			final Class<? extends AbstractChangePropertyAction<String>> clazz, final E from, final E to) {
		return compareProperty(accessor, String.class, clazz, from, to);
	}
}
