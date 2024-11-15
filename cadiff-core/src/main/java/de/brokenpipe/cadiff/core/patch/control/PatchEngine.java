package de.brokenpipe.cadiff.core.patch.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PatchEngine {

	private final BpmnModelInstance target;
	private final ChangeSet changeSet;

	public ChangeSet applyChanges() {
		final List<Action> rejectedChanges = new ArrayList<>();

		for (final Action action : this.changeSet.changes()) {
			action.getPatcher().accept(this.target);
		}

		return new ChangeSet(rejectedChanges);
	}
}
