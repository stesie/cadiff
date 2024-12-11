package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.stream.Stream;

interface PropertyComparator<E extends BaseElement, T> extends Comparator {

	default Stream<Action> compareProperty(final Function<E, T> accessor, final Class<T> propertyClass,
			final Class<? extends ChangePropertyAction<T>> clazz, final CompareContext<? extends E> compareContext) {

		return compareProperty(accessor, (id, oldValue, newValue) -> {
			try {
				final var constructor = clazz.getConstructor(String.class, propertyClass, propertyClass);
				return constructor.newInstance(id, oldValue, newValue);
			} catch (final InstantiationException | IllegalAccessException | InvocationTargetException |
					NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}, compareContext);
	}

	// FIXME shouldn't we apply `accessor` via `compareContext.map` in the caller already !?
	default Stream<Action> compareProperty(final Function<E, T> accessor, final ActionCreator<T> actionCreator,
			final CompareContext<? extends E> compareContext) {

		if (accessor.apply(compareContext.from()) == null && accessor.apply(compareContext.to()) == null) {
			return Stream.empty();
		}

		if (accessor.apply(compareContext.from()) == null) {
			return Stream.of(actionCreator.apply(compareContext.to().getId(), null, accessor.apply(compareContext.to())));
		}

		if (accessor.apply(compareContext.from()).equals(accessor.apply(compareContext.to()))) {
			return Stream.empty();
		}

		return Stream.of(actionCreator.apply(compareContext.to().getId(), accessor.apply(compareContext.from()), accessor.apply(
				compareContext.to())));
	}

	@FunctionalInterface
	interface ActionCreator<V> {
		 Action apply(final String id, final V from, final V to);
	}
}
