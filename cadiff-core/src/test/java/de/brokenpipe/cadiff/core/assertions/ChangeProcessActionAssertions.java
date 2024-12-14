package de.brokenpipe.cadiff.core.assertions;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.actions.processes.ChangeSubProcessAction;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RequiredArgsConstructor
public class ChangeProcessActionAssertions {

	private final String id;
	private final Collection<Action> actions;

	public ChangeProcessActionAssertions(final ChangeProcessAction process) {
		id = process.id();
		actions = process.actions();
	}

	public ChangeProcessActionAssertions(final ChangeSubProcessAction process) {
		id = process.id();
		actions = process.actions();
	}

	public ChangeProcessActionAssertions assertId(final String expectedId) {
		assertEquals(expectedId, id, "Process id does not match");
		return this;
	}

	public ActionCollectionAssertions actions() {
		return new ActionCollectionAssertions(actions);
	}
}
