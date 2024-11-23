package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.control.ChangeSetPrinter;
import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.processes.ChangeSubProcessAction;

public class ChangeSubProcessActionPrinter extends AbstractActionPrinter {

	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeSubProcessAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeSubProcessAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);

		indent += 8;

		new ChangeSetPrinter(context.forSubProcess(change.actions())).printAll();

		indent -= 8;
	}

}
