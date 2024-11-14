package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.Optional;

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
		else {
			throw new NotImplementedException();
		}
	}

	protected void printOldElementName(final ActionPrintContext context, final String targetId) {
		final String origTargetId = Optional.ofNullable(context.getIdMapBackward().get(targetId)).orElse(targetId);
		printElementName(context.getFrom().getModelElementById(origTargetId));
	}

	protected void removeChangeNameById(final ActionPrintContext context, final String id) {
		context.findChangesForId(id)
				.filter(x -> x instanceof ChangeNameAction)
				.findFirst()
				.ifPresent(x -> context.getActions().remove(x));
	}

	protected void printSteps(final ActionPrintContext context, final List<AddAction.Step> steps) {
		if (isNewElement(context, steps.getFirst().id())) {
			System.out.print(ansi().fg(ChangeType.ADD.getColor()));
			removeChangeNameById(context, steps.getFirst().id());
		}
		greenIfNewNode(context, steps.getFirst().id());
		printElementName(context.getTo().getModelElementById(steps.getFirst().id()));
		System.out.println(ansi().reset());
		new ChangePropertyActionPrinter().printAttributeChangesForId(context, steps.getFirst().id());

		for (int i = 2; i < steps.size() - 1; i += 2) {
			final String edgeId = steps.get(i - 1).id();
			final String elementId = steps.get(i).id();
			final ModelElementInstance element = context.getTo().getModelElementById(elementId);

			indent();
			System.out.println("    |");
			new ChangePropertyActionPrinter().printAttributeChangesForId(context, edgeId, "    | ");
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
		new ChangePropertyActionPrinter().printAttributeChangesForId(context,
				steps.get(steps.size() - 2).id(), "    | ");
		indent();
		System.out.println("    |");
		indent();
		System.out.print("    `-> ");
		greenIfNewNode(context, steps.getLast().id());
		printElementName(context.getTo().getModelElementById(steps.getLast().id()));
		System.out.println(ansi().reset());

		indent += 4;
		removeChangeNameById(context, steps.getLast().id());
		new ChangePropertyActionPrinter().printAttributeChangesForId(context, steps.getLast().id());

		indent = 0;
	}

	protected void greenIfNewNode(final ActionPrintContext context, final String id) {
		if (isNewElement(context, id)) {
			System.out.print(ansi().fg(ChangeType.ADD.getColor()));
		}
	}

	protected boolean isNewElement(final ActionPrintContext context, final String id) {
		final String fromId = Optional.ofNullable(context.getIdMapBackward().get(id)).orElse(id);
		return context.getFrom().getModelElementById(fromId) == null;
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
