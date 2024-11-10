package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class AbstractActionPrinter implements ActionPrinter {

	protected static int indent = 0;

	protected void indent() {
		if (indent > 0) {
			System.out.printf("%" + indent + "s", "");
		}
	}

	protected void printChangeLine(final String attributeName, final Object oldValue, final Object newValue, final String leader) {

		indent();
		System.out.printf("%s%23s : ", leader, attributeName);
		System.out.print(ansi().fg(Ansi.Color.RED).a("%-40s".formatted(oldValue)).reset());
		System.out.print(" -> ");
		System.out.print(ansi().fg(Ansi.Color.GREEN).a("%-40s".formatted(newValue)).reset());
		System.out.println();
	}

	protected void printElementLine(final ActionPrintContext context, final String attributeName, final String id, final ChangeType type) {
		final ModelElementInstance element = type == ChangeType.DELETE
			? context.getFrom().getModelElementById(id)
			: context.getTo().getModelElementById(id);

		indent();
		System.out.printf(" -> %23s : ", attributeName);
		printElementName(element);
		System.out.println();
	}

	protected void startBlock(final ActionPrintContext context, final String id, final ChangeType type) {
		final ModelElementInstance element = type == ChangeType.DELETE
				? context.getFrom().getModelElementById(id)
				: context.getTo().getModelElementById(id);

		System.out.println();

		indent();
		System.out.print(ansi().reset().fg(type.getColor()).a("%6s : ".formatted(type.getVerb()))
				.bold().a("%18s".formatted(element.getElementType().getTypeName())).boldOff()
				.a(" : "));
		printElementName(element);

		System.out.println(ansi().reset());
	}

	protected void printElementName(final ModelElementInstance element) {
		if (element instanceof final FlowElement flowElement && flowElement.getName() != null) {
			System.out.print(ansi().bold().a(flowElement.getName().replace('\n', ' ')).boldOff());
			System.out.printf(" (%s)", flowElement.getId());
		}
		else if (element instanceof final BaseElement baseElement) {
			System.out.print(ansi().bold().a(baseElement.getId()).boldOff());
		}
	}

	protected void removeChangeNameById(final ActionPrintContext context, final String id) {
		context.findChangesForId(id)
				.filter(x -> x instanceof ChangeNameAction)
				.findFirst()
				.ifPresent(x -> context.getChanges().remove(x));
	}


	@Getter
	@RequiredArgsConstructor
	public enum ChangeType {
		UPDATE("Update", Ansi.Color.DEFAULT),
		ADD("Add", Ansi.Color.GREEN),
		DELETE("Delete", Ansi.Color.RED);

		private final String verb;
		private final Ansi.Color color;
	}

}
