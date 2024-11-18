package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddSimpleSequenceFlowPatcher extends AbstractPatcher implements Patcher {

	private final AddSimpleSequenceFlowAction action;

	@Override
	public void accept(final PatcherContext context) {
		addSequenceFlow(context, action.id(), action.sourceId(), action.targetId(), action.waypoints());
	}

}
