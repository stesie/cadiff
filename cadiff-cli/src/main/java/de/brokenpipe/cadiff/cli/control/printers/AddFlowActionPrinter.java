package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddFlowAction;
import de.brokenpipe.cadiff.core.actions.ChangeAttachedToAction;

public class AddFlowActionPrinter extends AbstractActionPrinter implements ActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddFlowAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AddFlowAction) action;

		System.out.println();
		indent();
		if (change.attachedToId().isPresent()) {
			System.out.print("Add new boundary event to: ");
			printElementName(context.getTo().getModelElementById(change.attachedToId().get()));
			System.out.println();

			indent();
			System.out.print("    Boundary Event: ");

			// don't repeat attachedTo (we just printed it above)
			removeByIdAndActionType(context, change.steps().getFirst().id(), ChangeAttachedToAction.class);
		} else {
			System.out.println("Create new flow:");
		}

		printSteps(context, change.steps());
	}

}
