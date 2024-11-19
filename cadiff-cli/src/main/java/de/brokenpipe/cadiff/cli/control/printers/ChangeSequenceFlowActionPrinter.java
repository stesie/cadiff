package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeSequenceFlowAction;

public class ChangeSequenceFlowActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeSequenceFlowAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeSequenceFlowAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);

		if (!change.oldSourceId().equals(change.newSourceId())) {
			printChangeLine("source", change.oldSourceId(), change.newSourceId(), " -> ");
		}

		if (!change.oldTargetId().equals(change.newTargetId())) {
			printChangeLine("target", change.oldTargetId(), change.newTargetId(), " -> ");
		}
	}

}
