package de.brokenpipe.cadiff.core.assertions;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeAttachedToAction;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.actions.processes.ChangeSubProcessAction;
import lombok.Getter;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionCollectionAssertions {

	@Getter
	private final Collection<Action> actions;

	private final Iterator<Action> actionsIterator;

	public ActionCollectionAssertions(final Collection<Action> actions) {
		this.actions = actions;
		this.actionsIterator = actions.iterator();
	}

	public <T extends Action> ActionAssertions<T> assertExactlyOneInstanceOf(final Class<T> clazz) {
		return new ActionAssertions<>(findExactlyOneInstanceOf(clazz));
	}

	public ActionCollectionAssertions assertSize(final int count) {
		assertEquals(count, actions.size(), "expected %d actions, but got %d"
				.formatted(Integer.valueOf(count), Integer.valueOf(actions.size())));
		return this;
	}

	public ActionCollectionAssertions assertSize(final int count, final String message) {
		assertEquals(count, actions.size(), message);
		return this;
	}

	public ChangeProcessActionAssertions assertExactlyOneChangeProcessAction() {
		final var process = findExactlyOneInstanceOf(ChangeProcessAction.class);
		return new ChangeProcessActionAssertions(process);
	}

	public ChangeProcessActionAssertions assertExactlyOneChangeSubProcessAction() {
		final var process = findExactlyOneInstanceOf(ChangeSubProcessAction.class);
		return new ChangeProcessActionAssertions(process);
	}

	public ActionAssertions<Action> nextAction() {
		return new ActionAssertions<>(actionsIterator.next());
	}

	private <T> T findExactlyOneInstanceOf(final Class<T> clazz) {
		final var matches = actions.stream().filter(clazz::isInstance).toList();
		assertEquals(1, matches.size(), "only one action of type '%s' expected, but got %d"
				.formatted(clazz.getSimpleName(), Long.valueOf(matches.size())));
		return clazz.cast(matches.getFirst());
	}

	public void assertNoInstanceOf(final Class<ChangeAttachedToAction> clazz) {
		assertTrue(actions.stream().noneMatch(clazz::isInstance), "no action of type '%s' expected"
				.formatted(clazz.getSimpleName()));
	}

}
