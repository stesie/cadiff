package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

/**
 * Interface for action printers that supports printing just "change lines" for actions that are related to a single id.
 * This is used to bundle multiple changes for the same id into a single block.
 */
public interface SingleIdRelatedActionPrinter extends ActionPrinter {

	void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final AbstractActionPrinter.ChangeType changeType);
}
