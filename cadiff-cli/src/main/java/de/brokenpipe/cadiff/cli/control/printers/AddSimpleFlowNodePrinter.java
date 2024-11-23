package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;

public class AddSimpleFlowNodePrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddSimpleFlowNodeAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AddSimpleFlowNodeAction) action;

		startBlock(context, change.id(), ChangeType.ADD);

		// remove name change for this element, we've just printed the new name
		removeChangeNameById(context, change.id());

		printAttributeChangesForId(context, change.id(), ChangeType.ADD);
	}

}
