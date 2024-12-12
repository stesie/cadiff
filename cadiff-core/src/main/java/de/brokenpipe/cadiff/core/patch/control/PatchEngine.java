package de.brokenpipe.cadiff.core.patch.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
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
			action.patcher().accept(PatcherContext.of(this.target, this.target.getDefinitions()));
		}

		return new ChangeSet(rejectedChanges);
	}
}
