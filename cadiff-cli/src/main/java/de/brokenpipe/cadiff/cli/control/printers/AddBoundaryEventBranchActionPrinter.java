package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddBoundaryEventBranchAction;

public class AddBoundaryEventBranchActionPrinter extends AbstractActionPrinter implements ActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddBoundaryEventBranchAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AddBoundaryEventBranchAction) action;

		System.out.println();
		System.out.print("Add new boundary event to: ");
		printElementName(context.getTo().getModelElementById(change.attachedToId()));
		System.out.println();

		System.out.print("    Boundary Event: ");

		printSteps(context, change.steps());
	}

}
