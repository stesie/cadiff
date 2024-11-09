package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;

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
		context.findChangesForId(change.id())
				.filter(x -> x instanceof ChangeNameAction)
				.findFirst()
				.ifPresent(x -> context.getChanges().remove(x));

		new ChangePropertyActionPrinter().printAttributeChangesForId(context, change.id());
	}
}