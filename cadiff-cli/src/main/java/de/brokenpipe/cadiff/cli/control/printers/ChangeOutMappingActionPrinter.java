package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAction;
import de.brokenpipe.cadiff.core.actions.SingleIdRelatedAction;

import java.util.Optional;

public class ChangeOutMappingActionPrinter extends AbstractActionPrinter implements SingleIdRelatedActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof ChangeOutMappingAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (ChangeOutMappingAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);
		writeLine(context, change, " -> ", ChangeType.UPDATE);

		printAttributeChangesForId(context, change.id(), ChangeType.UPDATE);
	}

	@Override
	public void writeLine(final ActionPrintContext context, final SingleIdRelatedAction action, final String leader,
			final ChangeType changeType) {
		final var change = (ChangeOutMappingAction) action;

		printChangeLine("outMapping[%s]".formatted(change.targetName()),
				Optional.ofNullable(change.oldValue()).map(ChangeOutMappingAction.Config::source).orElse(null),
				Optional.ofNullable(change.newValue()).map(ChangeOutMappingAction.Config::source).orElse(null),
				leader, changeType);

		if (change.newValue() == null) {
			// if mapping is removed, don't bother printing isExpression/local differences
			return;
		}

		if (change.oldValue() == null) {
			// mapping is added, print non-default values
			if (change.newValue().isSourceExpression()) {
				printChangeLine("`-> isSourceExpression", null, Boolean.TRUE, leader, changeType);
			}

			if (change.newValue().local()) {
				printChangeLine("`-> local", null, Boolean.TRUE, leader, changeType);
			}

			return;
		}

		if (change.oldValue().isSourceExpression() != change.newValue().isSourceExpression()) {
			printChangeLine("`-> isSourceExpression", Boolean.valueOf(change.oldValue().isSourceExpression()),
					Boolean.valueOf(change.newValue().isSourceExpression()), leader, changeType);
		}

		if (change.oldValue().local() != change.newValue().local()) {
			printChangeLine("`-> local", Boolean.valueOf(change.oldValue().local()),
					Boolean.valueOf(change.newValue().local()), leader, changeType);
		}
	}
}
