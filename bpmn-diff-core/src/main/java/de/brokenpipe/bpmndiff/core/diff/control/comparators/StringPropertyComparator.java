package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.AbstractChangeStringPropertyAction;
import de.brokenpipe.bpmndiff.core.actions.Action;
import lombok.SneakyThrows;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.stream.Stream;

interface StringPropertyComparator<E extends FlowElement> extends Comparator {

	@SneakyThrows(value = {
			InstantiationException.class,
			InvocationTargetException.class,
			IllegalAccessException.class,
			NoSuchMethodException.class })
	default Stream<Action> compareStringProperty(final Function<E, String> accessor,
			final Class<? extends AbstractChangeStringPropertyAction> clazz, final E from, final E to) {

		final var constructor = clazz.getConstructor(String.class, String.class, String.class);

		if (accessor.apply(from) == null && accessor.apply(to) == null) {
			return Stream.empty();
		}

		if (accessor.apply(from) == null) {
			return Stream.of(constructor.newInstance(to.getId(), null, accessor.apply(to)));
		}

		if (accessor.apply(from).equals(accessor.apply(to))) {
			return Stream.empty();
		}

		return Stream.of(constructor.newInstance(from.getId(), accessor.apply(from), accessor.apply(to)));
	}
}
