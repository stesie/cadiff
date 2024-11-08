package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocumentWalker {

	private final Definitions from;
	private final Definitions to;

	public Stream<Action> walk() {

		return walkProcesses();
	}

	private Stream<Action> walkProcesses() {
		final Collection<Process> fromProcesses = findProcesses(from);
		final Collection<Process> toProcesses = findProcesses(to);

		return (new ProcessWalker(fromProcesses, toProcesses).walk());
	}

	private List<Process> findProcesses(final Definitions definition) {
		return definition.getRootElements().stream()
				.filter(x -> x instanceof Process)
				.map(Process.class::cast)
				.toList();
	}
}
