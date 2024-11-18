package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddSimpleFlowNodePatcher extends AbstractPatcher implements Patcher {

	private final AddSimpleFlowNodeAction action;

	@Override
	public void accept(final PatcherContext context) {
		addFlowElement(context, action.id(), action.elementTypeName(), action.bounds());

	}

}
