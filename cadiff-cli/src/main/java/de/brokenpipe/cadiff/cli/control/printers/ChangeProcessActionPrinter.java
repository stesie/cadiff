package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.control.ChangeSetPrinter;
import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeProcessAction;

public class ChangeProcessActionPrinter extends AbstractActionPrinter {

	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeProcessAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeProcessAction) action;

		startBlock(context, change.id(), AbstractActionPrinter.ChangeType.UPDATE);
		new ChangePropertyActionPrinter().printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);

		new ChangeSetPrinter(context.forSubProcess(change.actions())).printAll();

	}

}
