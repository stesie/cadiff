package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeIdAction;

public class ChangeIdActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeIdAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		if (!context.isPrintIdChanges()) {
			return;
		}

		final var change = (ChangeIdAction) action;

		startBlock(context, change.newId(), AbstractActionPrinter.ChangeType.ADD);
		printChangeLine("id", change.oldId(), change.newId());

		// remove name change for this element, we've just printed the new name
		removeChangeNameById(context, change.newId());

		new ChangePropertyActionPrinter().printAttributeChangesForId(context, change.newId());
	}
}
