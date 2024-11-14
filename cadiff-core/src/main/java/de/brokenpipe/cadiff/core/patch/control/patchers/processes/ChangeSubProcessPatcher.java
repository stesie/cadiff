package de.brokenpipe.cadiff.core.patch.control.patchers.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeSubProcessAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class ChangeSubProcessPatcher implements Patcher {

	final ChangeSubProcessAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {

		for (final Action action : action.actions()) {
			action.getPatcher().accept(bpmnModelInstance);
		}

	}
}
