package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;

import java.util.function.BiConsumer;

public interface ActionPrinter extends BiConsumer<ActionPrintContext, Action> {

	boolean supports(Action action);
}
