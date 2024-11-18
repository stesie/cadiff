package de.brokenpipe.cadiff.core.patch.control.patchers.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeSubProcessAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.AbstractPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.SubProcess;

@RequiredArgsConstructor
public class ChangeSubProcessPatcher extends AbstractPatcher implements Patcher {

	final ChangeSubProcessAction action;

	@Override
	public void accept(final PatcherContext context) {

		final SubProcess subProcess = findTargetWithType(context, action.id(), SubProcess.class);
		final PatcherContext innerContext = context.withContainerElem(subProcess);

		for (final Action a : action.actions()) {
			a.getPatcher().accept(innerContext);
		}

	}
}
