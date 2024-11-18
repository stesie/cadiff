package de.brokenpipe.cadiff.core.patch.control.patchers.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

@RequiredArgsConstructor
public class ChangeProcessPatcher implements Patcher {

	final ChangeProcessAction action;

	@Override
	public void accept(final PatcherContext context) {

		for (final Action a : action.actions()) {
			final ModelElementInstance target = context.getModelInstance().getModelElementById(action.id());

			if (target == null) {
				throw new TargetElementNotFoundException(action.id());
			}

			if (target instanceof final Process process) {
				a.getPatcher().accept(context.withProcess(process));
				return;
			}

			throw new IllegalStateException("Target element is not a Process");

		}

	}
}
