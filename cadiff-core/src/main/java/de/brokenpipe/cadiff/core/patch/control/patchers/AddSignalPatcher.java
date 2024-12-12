package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSignalAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Signal;

@RequiredArgsConstructor
public class AddSignalPatcher extends AbstractPatcher implements Patcher {

	private final AddSignalAction action;

	@Override
	public void accept(final PatcherContext context) {
		addElement(context, action.id(), context.getModelInstance().getModel().getType(Signal.class));

	}

}
