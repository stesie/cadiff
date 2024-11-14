package de.brokenpipe.cadiff.core.patch.control.patchers.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class ChangeProcessPatcher implements Patcher {

	final ChangeProcessAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {

		for (final Action action : action.actions()) {
			action.getPatcher().accept(bpmnModelInstance);
		}

	}
}
