package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.cadiff.core.actions.Action;
import lombok.SneakyThrows;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.stream.Stream;

interface PropertyComparator<E extends BaseElement, T> extends Comparator {

	@SneakyThrows(value = {
			InstantiationException.class,
			InvocationTargetException.class,
			IllegalAccessException.class,
			NoSuchMethodException.class })
	default Stream<Action> compareProperty(final Function<E, T> accessor, final Class<T> propertyClass,
			final Class<? extends AbstractChangePropertyAction<T>> clazz, final E from, final E to) {

		final var constructor = clazz.getConstructor(String.class, propertyClass, propertyClass);

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
