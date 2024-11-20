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

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

	public void printAttributeChangesForId(final ActionPrintContext context, final String id, final ChangeType changeType) {
		printAttributeChangesForId(context, id, " -> ", changeType);
	}

	public void printAttributeChangesForId(final ActionPrintContext context, final String id, final String leader,
			final ChangeType changeType) {
		context.findChangesForId(id)
				.filter(this::supports)
				.toList()
				.forEach(c -> {
					writeLine(context, (ChangePropertyAction<?>) c, leader, changeType);
					context.getActions().remove(c);
				});
	}

	private void writeLine(final ActionPrintContext context, final ChangePropertyAction<?> change, final String leader,
			final ChangeType changeType) {
		printChangeLine(change.attributeName(), change.oldValue(), change.newValue(), leader, changeType);
	}

}
