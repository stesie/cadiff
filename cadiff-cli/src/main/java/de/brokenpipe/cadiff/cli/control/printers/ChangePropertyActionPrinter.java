package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;

public class ChangePropertyActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangePropertyAction<?>;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangePropertyAction<?>) action;

		startBlock(context, change.getId(), ChangeType.UPDATE);
		writeLine(context, change, " -> ");

		printAttributeChangesForId(context, change.getId());
	}

	public void printAttributeChangesForId(final ActionPrintContext context, final String id) {
		printAttributeChangesForId(context, id, " -> ");
	}

	public void printAttributeChangesForId(final ActionPrintContext context, final String id, final String leader) {
		context.findChangesForId(id)
				.filter(this::supports)
				.toList()
				.forEach(c -> {
					writeLine(context, (ChangePropertyAction<?>) c, leader);
					context.getActions().remove(c);
				});
	}

	private void writeLine(final ActionPrintContext context, final ChangePropertyAction<?> change, final String leader) {
		printChangeLine(change.getAttributeName(), change.getOldValue(), change.getNewValue(), leader);
	}

}
