package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeInMappingAllAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

import java.util.Objects;

public class ChangeInMappingAllActionPrinter extends AbstractActionPrinter implements SingleIdRelatedActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeInMappingAllAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeInMappingAllAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

	@Override
	public void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final ChangeType changeType) {
		final var change = (ChangeInMappingAllAction) action;

		printChangeLine("propagate all variables (in)", Boolean.valueOf(change.oldValue().enabled()),
				Boolean.valueOf(change.newValue().enabled()), leader, changeType);

		if (change.newValue().enabled() &&
				!Objects.equals(change.oldValue().local(), change.newValue().local())) {
			printChangeLine("`-> local", change.oldValue().local(), change.newValue().local(), leader,
					changeType);
		}
	}
}
