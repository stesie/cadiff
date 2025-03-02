package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.assertions.ActionCollectionAssertions;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.patch.boundary.PatchCommand;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public abstract class AbstractComparePatchIT {

	protected final BpmnModelInstance from;
	protected final BpmnModelInstance to;

	protected abstract void verifyForwardChanges(final ActionCollectionAssertions changes);

	@Test
	void shouldCreateCorrectForwardChanges() {
		final ChangeSet changeSet = new DiffCommand(from, to).execute();
		verifyForwardChanges(new ActionCollectionAssertions(changeSet.changes()));
	}

	@Test
	void shouldCreateEmptyChangeSetAfterApplyingChanges() {
		final var fromClone = from.clone();

		final ChangeSet changeSet = new DiffCommand(from, to).execute();
		new PatchCommand(fromClone, changeSet).execute();

		final ChangeSet emptyChangeSet = new DiffEngine(fromClone, to).compareDocuments();
		assertTrue(emptyChangeSet.changes().isEmpty(),
				"change set should be empty, after applying changes. but got: %s".formatted(emptyChangeSet));
	}

	@Test
	void shouldCreateEmptyChangeSetAfterApplyingReverseChanges() {
		final var toClone = to.clone();

		final ChangeSet changeSet = new DiffCommand(to, from).execute();
		new PatchCommand(toClone, changeSet).execute();

		final ChangeSet emptyChangeSet = new DiffEngine(toClone, from).compareDocuments();
		assertEquals(0, emptyChangeSet.changes().size());
	}

}
