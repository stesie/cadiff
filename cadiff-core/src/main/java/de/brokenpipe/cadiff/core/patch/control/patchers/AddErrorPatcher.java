package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddErrorAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Error;

@RequiredArgsConstructor
public class AddErrorPatcher extends AbstractPatcher implements Patcher {

	private final AddErrorAction action;

	@Override
	public void accept(final PatcherContext context) {
		addElement(context, action.id(), context.getModelInstance().getModel().getType(Error.class));

	}

}
