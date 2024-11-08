package de.brokenpipe.bpmndiff.core.diff.control;

import de.brokenpipe.bpmndiff.core.diff.entity.ChangeSet;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

/**
 * Orchestrates the diffing of two BPMN documents.
 * <ol>
 *     <li>find renamed instances</li>
 *     <li>walk all the nodes</li>
 * </ol>
 */
@RequiredArgsConstructor
public class DiffEngine {

	private final BpmnModelInstance from;
	private final BpmnModelInstance to;

	public ChangeSet compareDocuments() {
		return new ChangeSet(new DocumentWalker(from.getDefinitions(), to.getDefinitions()).walk().toList());
	}
}
