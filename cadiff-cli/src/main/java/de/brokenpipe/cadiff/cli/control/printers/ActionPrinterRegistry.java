package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.core.actions.Action;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

@RequiredArgsConstructor
public class ActionPrinterRegistry {

	private final List<ActionPrinter> actionPrinters;

	public static final ActionPrinterRegistry INSTANCE = ActionPrinterRegistry.init();

	private static ActionPrinterRegistry init() {
		return new ActionPrinterRegistry(
				ServiceLoader.load(ActionPrinter.class).stream()
						.map(ServiceLoader.Provider::get)
						.toList());
	}

	public Optional<ActionPrinter> find(final Action action) {
		return actionPrinters.stream()
				.filter(ap -> ap.supports(action))
				.findFirst();
	}

}
