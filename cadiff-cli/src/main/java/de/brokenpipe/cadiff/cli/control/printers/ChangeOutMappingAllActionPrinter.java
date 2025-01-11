package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAllAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

import java.util.Objects;

public class ChangeOutMappingAllActionPrinter extends AbstractActionPrinter implements SingleIdRelatedActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeOutMappingAllAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeOutMappingAllAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

	@Override
	public void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final ChangeType changeType) {
		final var change = (ChangeOutMappingAllAction) action;

		printChangeLine("propagate all variables (out)", Boolean.valueOf(change.oldValue().enabled()),
				Boolean.valueOf(change.newValue().enabled()), leader, changeType);

		if (!change.newValue().enabled()) {
			// if mapping is removed, don't bother printing local differences
			return;
		}

		if (!change.oldValue().enabled() && Boolean.FALSE.equals(change.newValue().local())) {
			// mapping is added, print only non-default values (and local remains off)
			return;
		}

		if (!Objects.equals(change.oldValue().local(), change.newValue().local())) {
			printChangeLine("`-> local", change.oldValue().local(), change.newValue().local(), leader,
					changeType);
		}
	}
}
