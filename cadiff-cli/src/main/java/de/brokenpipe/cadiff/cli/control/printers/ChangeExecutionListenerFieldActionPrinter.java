package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeExecutionListenerFieldAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

import java.util.Optional;

public class ChangeExecutionListenerFieldActionPrinter extends AbstractActionPrinter implements SingleIdRelatedActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeExecutionListenerFieldAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeExecutionListenerFieldAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

	@Override
	public void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final ChangeType changeType) {
		final var change = (ChangeExecutionListenerFieldAction) action;

		printChangeLine("executionListenerField[%s]".formatted(change.fieldName()),
				Optional.ofNullable(change.oldValue()).map(ChangeExecutionListenerFieldAction.Config::source).orElse(null),
				Optional.ofNullable(change.newValue()).map(ChangeExecutionListenerFieldAction.Config::source).orElse(null),
				leader, changeType);

		if (change.newValue() == null) {
			// if field is removed, don't bother printing isSourceExpression differences
			return;
		}

		if (change.oldValue() == null) {
			// field is added, print non-default values
			if (change.newValue().isSourceExpression()) {
				printChangeLine("`-> isSourceExpression", null, Boolean.TRUE, leader, changeType);
			}
			return;
		}

		if (change.oldValue().isSourceExpression() != change.newValue().isSourceExpression()) {
			printChangeLine("`-> isSourceExpression", Boolean.valueOf(change.oldValue().isSourceExpression()),
					Boolean.valueOf(change.newValue().isSourceExpression()), leader, changeType);
		}
	}
}
