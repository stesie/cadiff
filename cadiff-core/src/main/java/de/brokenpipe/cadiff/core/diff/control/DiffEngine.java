package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
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
		return new ChangeSet(new DocumentWalker(new CompareContext<>(from, null, from.getDefinitions(), to.getDefinitions())).walk().toList());
	}
}
