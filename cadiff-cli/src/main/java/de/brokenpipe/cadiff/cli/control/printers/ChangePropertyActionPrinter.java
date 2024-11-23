package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

public class ChangePropertyActionPrinter extends AbstractActionPrinter implements SingleIdRelatedActionPrinter {

	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangePropertyAction<?>;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangePropertyAction<?>) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}


	@Override
	public void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final ChangeType changeType) {
		final var change = (ChangePropertyAction<?>) action;

		printChangeLine(change.attributeName(), change.oldValue(), change.newValue(), leader, changeType);
	}

}
