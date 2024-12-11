package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.patch.boundary.PatchCommand;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public abstract class AbstractComparePatchIT {

	final BpmnModelInstance from;
	final BpmnModelInstance to;

	protected abstract void verifyForwardChanges(final List<Action> changes);

	@Test
	void shouldCreateCorrectChanges() {
		final ChangeSet changeSet = new DiffCommand(from, to).execute();
		verifyForwardChanges(changeSet.changes());
	}

	@Test
	void shouldCreateEmptyChangeSetAfterApplyingChanges() {
		final var fromClone = from.clone();

		final ChangeSet changeSet = new DiffCommand(from, to).execute();
		new PatchCommand(fromClone, changeSet).execute();

		final ChangeSet emptyChangeSet = new DiffEngine(fromClone, to).compareDocuments();
		assertEquals(0, emptyChangeSet.changes().size());
	}
}
