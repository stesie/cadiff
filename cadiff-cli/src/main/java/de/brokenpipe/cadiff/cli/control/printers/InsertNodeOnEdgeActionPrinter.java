package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;

public class InsertNodeOnEdgeActionPrinter extends AbstractActionPrinter implements ActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof InsertNodeOnEdgeAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (InsertNodeOnEdgeAction) action;

		System.out.println();
		System.out.println("Inserted node on edge:");

		printSteps(context, change.steps());
	}
}
