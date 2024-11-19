package de.brokenpipe.cadiff.core.patch.control.patchers.processes;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.AbstractPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Process;

@RequiredArgsConstructor
public class ChangeProcessPatcher extends AbstractPatcher implements Patcher {

	final ChangeProcessAction action;

	@Override
	public void accept(final PatcherContext context) {
		final Process process = findTargetWithType(context, action.id(), Process.class);
		final PatcherContext innerContext = context.withContainerElem(process);

		for (final Action a : action.actions()) {
			a.patcher().accept(innerContext);

		}

	}
}
