package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class AbstractActionPrinter implements ActionPrinter {

	protected void startChangeLine(final ActionPrintContext context, final String attributeName,
			final Object oldValue, final Object newValue) {

		System.out.printf(" -> %20s : ", attributeName);
		System.out.print(ansi().fg(Ansi.Color.RED).a("%-40s".formatted(oldValue)).reset());
		System.out.print(" -> ");
		System.out.print(ansi().fg(Ansi.Color.GREEN).a("%-40s".formatted(newValue)).reset());
		System.out.println();
	}

	protected void startBlock(final ActionPrintContext context, final String id, final ChangeType type) {
		final ModelElementInstance element = context.getTo().getModelElementById(id);

		System.out.println();

		final var color = switch (type) {
			case UPDATE -> Ansi.Color.DEFAULT;
			case ADD -> Ansi.Color.GREEN;
			case DELETE -> Ansi.Color.RED;
		};

		System.out.print(ansi().reset().fg(color).a("%6s : ".formatted(type.getVerb()))
				.bold().a("%15s".formatted(element.getElementType().getTypeName())).boldOff()
				.a(" : "));

		if (element instanceof final FlowElement flowElement) {
			System.out.print(ansi().bold().a(flowElement.getName()).boldOff());
			System.out.printf(" (%s)", flowElement.getId());
		}
		else if (element instanceof final BaseElement baseElement) {
			System.out.print(ansi().bold().a(baseElement.getId()).boldOff());
		}

		System.out.println(ansi().reset());
	}

	@Getter
	@RequiredArgsConstructor
	enum ChangeType {
		UPDATE("Update"),
		ADD("Add"),
		DELETE("Delete");

		private final String verb;

	}

}
