package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddBranchToGatewayAction;

public class AddBranchToGatewayActionPrinter extends AbstractActionPrinter implements ActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddBranchToGatewayAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AddBranchToGatewayAction) action;

		System.out.println();
		System.out.println("Added branch to gateway:");

		printSteps(context, change.steps());
	}

}
