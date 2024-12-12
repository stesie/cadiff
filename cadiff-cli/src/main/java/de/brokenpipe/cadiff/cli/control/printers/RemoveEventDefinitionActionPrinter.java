package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.RemoveEventDefinitionAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

public class RemoveEventDefinitionActionPrinter extends AbstractActionPrinter implements SingleIdRelatedActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof RemoveEventDefinitionAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (SingleIdRelatedAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

	@Override
	public void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final ChangeType changeType) {
		final var change = (RemoveEventDefinitionAction) action;

		indent();
		printChangeLine("event definition", change.eventDefinitionId(), null, leader, ChangeType.DELETE);
	}
}
