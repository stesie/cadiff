package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeInMappingAllAction;

import java.util.Objects;

public class ChangeInMappingAllActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeInMappingAllAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeInMappingAllAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);

		printChangeLine("propagate all variables (in)", change.oldValue().enabled(), change.newValue().enabled(), " -> ", ChangeType.DELETE);

		if (Boolean.TRUE == change.newValue().enabled() &&
				!Objects.equals(change.oldValue().local(), change.newValue().local())) {
			printChangeLine("`-> local", change.oldValue().local(), change.newValue().local(), " -> ", ChangeType.UPDATE);
		}

		new ChangePropertyActionPrinter().printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

}
