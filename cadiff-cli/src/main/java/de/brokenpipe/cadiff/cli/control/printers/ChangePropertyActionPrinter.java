package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.cadiff.core.actions.Action;

public class ChangePropertyActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AbstractChangePropertyAction<?>;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AbstractChangePropertyAction<?>) action;

		startBlock(context, change.getId(), ChangeType.UPDATE);
		writeLine(context, change);

		printAttributeChangesForId(context, change.getId());
	}

	public void printAttributeChangesForId(final ActionPrintContext context, final String id) {
		context.findChangesForId(id)
				.filter(this::supports)
				.toList()
				.forEach(c -> {
					writeLine(context, (AbstractChangePropertyAction<?>) c);
					context.getChanges().remove(c);
				});
	}

	private void writeLine(final ActionPrintContext context, final AbstractChangePropertyAction<?> change) {
		final String attributeName = extractAttributeName(change);
		startChangeLine(context, attributeName, change.getOldValue(), change.getNewValue());
	}

	private static String extractAttributeName(final AbstractChangePropertyAction<?> change) {
		final String attributeName = change.getClass().getSimpleName()
				.replace("Change", "")
				.replace("Camunda", "")
				.replace("Action", "");
		return Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
	}

}
