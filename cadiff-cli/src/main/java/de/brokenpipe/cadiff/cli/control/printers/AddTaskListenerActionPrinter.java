package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddTaskListenerAction;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class AddTaskListenerActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddTaskListenerAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final var change = (AddTaskListenerAction) action;

		startBlock(context, change.id(), ChangeType.UPDATE);

		indent();
		System.out.printf("%s%23s : ", " -> ", "execution listener");
		System.out.print(ansi().fg(Ansi.Color.GREEN));
		writeProp("event", change.camundaEvent());
		writeProp("class", change.camundaClass());
		writeProp("delegateExpression", change.camundaDelegateExpression());
		writeProp("expression", change.camundaExpression());
		System.out.println(ansi().reset());

	}

	private void writeProp(final String attribute, final String value) {
		if (value == null) {
			return;
		}

		System.out.printf("%s=%s ", attribute, value);
	}

}
