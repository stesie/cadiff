package de.brokenpipe.cadiff.cli.control;

import de.brokenpipe.cadiff.cli.control.printers.ActionPrinterRegistry;
import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangeSetPrinter {

	private final ActionPrintContext context;

	public void printAll() {

		while (!context.getActions().isEmpty()) {
			final var change = context.getActions().removeFirst();
			final var printer = ActionPrinterRegistry.INSTANCE.find(change);

			if (printer.isEmpty()) {
				throw new IllegalStateException("No printer found for action: " + change.getType());
			}

			printer.get().accept(context, change);
		}

	}
}
