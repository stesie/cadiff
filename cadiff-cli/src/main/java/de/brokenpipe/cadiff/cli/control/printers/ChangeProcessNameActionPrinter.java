package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessNameAction;

public class ChangeProcessNameActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeProcessNameAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeProcessNameAction) action;

		startBlock(context, change.getId(), ChangeType.UPDATE);
		printChangeLine("name", change.getOldValue(), change.getNewValue());

	}

}
