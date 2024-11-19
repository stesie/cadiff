package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.stream.Stream;

interface PropertyComparator<E extends BaseElement, T> extends Comparator {

	default Stream<Action> compareProperty(final Function<E, T> accessor, final Class<T> propertyClass,
			final Class<? extends ChangePropertyAction<T>> clazz, final E from, final E to) {

		return compareProperty(accessor, (id, oldValue, newValue) -> {
			try {
				final var constructor = clazz.getConstructor(String.class, propertyClass, propertyClass);
				return constructor.newInstance(id, oldValue, newValue);
			} catch (final InstantiationException | IllegalAccessException | InvocationTargetException |
					NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}, from, to);
	}

	default Stream<Action> compareProperty(final Function<E, T> accessor, final ActionCreator<T> actionCreator,
			final E from, final E to) {
		if (accessor.apply(from) == null && accessor.apply(to) == null) {
			return Stream.empty();
		}

		if (accessor.apply(from) == null) {
			return Stream.of(actionCreator.apply(to.getId(), null, accessor.apply(to)));
		}

		if (accessor.apply(from).equals(accessor.apply(to))) {
			return Stream.empty();
		}

		return Stream.of(actionCreator.apply(from.getId(), accessor.apply(from), accessor.apply(to)));
	}

	@FunctionalInterface
	interface ActionCreator<V> {
		 Action apply(final String id, final V from, final V to);
	}
}
