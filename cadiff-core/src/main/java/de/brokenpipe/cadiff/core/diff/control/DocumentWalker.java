package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.Error;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocumentWalker {

	private final CompareContext<Definitions> compareContext;

	public Stream<Action> walk() {

		return Stream.concat(walkErrors(), walkProcesses());
	}

	private Stream<Action> walkProcesses() {
		return (new ProcessWalker(compareContext.map(findRootElementsOfType(Process.class))).walk());
	}

	private Stream<Action> walkErrors() {
		return (new ErrorWalker(compareContext.map(findRootElementsOfType(Error.class))).walk());
	}

	private <T extends BaseElement> Function<Definitions, Collection<T>> findRootElementsOfType(final Class<T> clazz) {
		return definition -> definition.getRootElements().stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.toList();
	}

}
