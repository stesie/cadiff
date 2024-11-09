package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;

public class AddSimpleSequenceFlowPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddSimpleSequenceFlowAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AddSimpleSequenceFlowAction) action;

		startBlock(context, change.id(), ChangeType.ADD);
		printElementLine(context, "source", change.sourceId());
		printElementLine(context, "target", change.targetId());
	}
}
