package de.brokenpipe.cadiff.core.assertions;

import de.brokenpipe.cadiff.core.actions.Action;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

import java.util.function.Function;

@RequiredArgsConstructor
public class ActionAssertions<T extends Action> {

	private final T action;

	public <U extends Action> ActionAssertions<U> assertInstanceOf(final Class<U> clazz) {
		Assertions.assertInstanceOf(clazz, action, "Action is not an instance of %s".formatted(clazz.getSimpleName()));
		return new ActionAssertions<>(clazz.cast(action));
	}

	public <U> ActionAssertions<T> assertEquals(final U expectedValue, final Function<T, U> accessor) {
		Assertions.assertEquals(expectedValue, accessor.apply(action));
		return this;
	}
}
