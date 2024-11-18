package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteElementPatcher extends AbstractPatcher implements Patcher {

	private final DeleteElementAction action;

	@Override
	public void accept(final PatcherContext context) {
		deleteElement(context.getModelInstance(), action.id());
	}
}
