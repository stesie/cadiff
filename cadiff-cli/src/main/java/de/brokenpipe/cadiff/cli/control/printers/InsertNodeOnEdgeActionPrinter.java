package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.InsertNodeOnEdgeAction;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import static org.fusesource.jansi.Ansi.ansi;

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

		System.out.print("    Before: ");
		printElementName(context.getFrom().getModelElementById(change.steps().getFirst().id()));
		System.out.print(" --> ");
		printElementName(context.getFrom().getModelElementById(change.steps().getLast().id()));
		System.out.println();

		System.out.print("    After: ");
		printElementName(context.getTo().getModelElementById(change.steps().getFirst().id()));
		System.out.println();

		for (int i = 2; i < change.steps().size() - 1; i += 2) {
			final String elementId = change.steps().get(i).id();
			final ModelElementInstance element = context.getTo().getModelElementById(elementId);

			indent();
			System.out.println("    |");
			indent();
			System.out.print("    `-> ");
			System.out.print(ansi().reset().fg(ChangeType.ADD.getColor())
					.bold().a(element.getElementType().getTypeName()).boldOff()
					.a(" : "));
			printElementName(element);
			System.out.println(ansi().reset());

			indent += 4;

			removeChangeNameById(context, elementId);
			new ChangePropertyActionPrinter().printAttributeChangesForId(context, elementId);
		}

		indent();
		System.out.println("    |");
		indent();
		System.out.print("    `-> ");
		printElementName(context.getTo().getModelElementById(change.steps().getLast().id()));
		System.out.println();

		indent = 0;
	}
}
