package de.brokenpipe.cadiff.core.assertions;

import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class ChangeProcessActionAssertions {

	private final ChangeProcessAction process;

	public ChangeProcessActionAssertions assertId(final String name) {
		assertEquals(name, process.id(), "Process id does not match");
		return this;
	}

	public ActionCollectionAssertions actions() {
		return new ActionCollectionAssertions(process.actions());
	}
}
