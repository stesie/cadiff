package de.brokenpipe.cadiff.core.diff.boundary;

import de.brokenpipe.cadiff.core.diff.control.DiffEngine;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.io.File;

@RequiredArgsConstructor
public class DiffCommand {

	private final BpmnModelInstance from;
	private final BpmnModelInstance to;

	public DiffCommand(final File from, final File to) {
		this.from = Bpmn.readModelFromFile(from);
		this.to = Bpmn.readModelFromFile(to);
	}

	public ChangeSet execute() {
		return new DiffEngine(this.from, this.to).compareDocuments();
	}
}
