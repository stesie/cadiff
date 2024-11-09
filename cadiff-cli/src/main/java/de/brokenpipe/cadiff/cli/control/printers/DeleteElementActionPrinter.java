package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.DeleteElementAction;

import static org.fusesource.jansi.Ansi.ansi;

public class DeleteElementActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof DeleteElementAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (DeleteElementAction) action;

		final var type = ChangeType.DELETE;

		System.out.println();
		System.out.println(ansi().reset().fg(type.getColor()).a("%6s : ".formatted(type.getVerb()))
				.bold().a(change.id()).boldOff().reset());
		// TODO add old tree to context, then resolve info from old tree
	}
}
