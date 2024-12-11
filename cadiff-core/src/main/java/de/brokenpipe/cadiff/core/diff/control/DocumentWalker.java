package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocumentWalker {

	private final CompareContext<Definitions> compareContext;

	public Stream<Action> walk() {

		return walkProcesses();
	}

	private Stream<Action> walkProcesses() {
		return (new ProcessWalker(compareContext.map(this::findProcesses)).walk());
	}

	private List<Process> findProcesses(final Definitions definition) {
		return definition.getRootElements().stream()
				.filter(x -> x instanceof Process)
				.map(Process.class::cast)
				.toList();
	}
}
