package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.AbstractChangePropertyAction;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

import java.util.Optional;

public class ChangePropertyActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AbstractChangePropertyAction<?>;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AbstractChangePropertyAction<?>) action;
		printBlock(context, change.getId(), change);
	}

	public void printBlock(final ActionPrintContext context, final String id) {
		printBlock(context, id, null);
	}

	private void printBlock(final ActionPrintContext context, final String id, final AbstractChangePropertyAction<?> extra) {

		startBlock(context, id, ChangeType.UPDATE);

		Optional.ofNullable(extra).ifPresent(change -> writeLine(context, change));

		context.getChanges().stream()
				.filter(c -> c instanceof SingleIdRelatedAction)
				.map(SingleIdRelatedAction.class::cast)
				.filter(c -> c.getId().equals(id))
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
