package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;

public class ChangeIdActionPrinter implements ActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeIdAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		// TODO: add flag, and print id changes conditionally
	}
}
