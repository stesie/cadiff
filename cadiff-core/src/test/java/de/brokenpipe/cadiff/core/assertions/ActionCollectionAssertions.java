package de.brokenpipe.cadiff.core.assertions;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionCollectionAssertions {

	private final Collection<Action> actions;
	private final Iterator<Action> actionsIterator;

	public ActionCollectionAssertions(final Collection<Action> actions) {
		this.actions = actions;
		this.actionsIterator = actions.iterator();
	}

	public <T> T assertExactlyOneInstanceOf(final Class<T> clazz) {
		final var matches = actions.stream().filter(clazz::isInstance).toList();
		assertEquals(1, matches.size(), "only one action of type '%s' expected, but got %d"
				.formatted(clazz.getSimpleName(), Long.valueOf(matches.size())));
		return clazz.cast(matches.getFirst());
	}

	public ActionCollectionAssertions assertSize(final int count) {
		assertEquals(count, actions.size(), "expected %d actions, but got %d"
				.formatted(Integer.valueOf(count), Integer.valueOf(actions.size())));
		return this;
	}

	public ChangeProcessActionAssertions assertExactlyOneChangeProcessAction() {
		final var process = assertExactlyOneInstanceOf(ChangeProcessAction.class);
		return new ChangeProcessActionAssertions(process);
	}

	public ActionAssertions<Action> nextAction() {
		return new ActionAssertions<>(actionsIterator.next());
	}
}
