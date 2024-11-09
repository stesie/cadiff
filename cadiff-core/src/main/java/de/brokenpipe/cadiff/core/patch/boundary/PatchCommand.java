package de.brokenpipe.cadiff.core.patch.boundary;

import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.patch.control.PatchEngine;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class PatchCommand {

	private final BpmnModelInstance target;
	private final ChangeSet changeSet;

	public ChangeSet execute() {
		return new PatchEngine(this.target, this.changeSet).applyChanges();
	}
}
