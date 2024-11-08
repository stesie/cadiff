package de.brokenpipe.bpmndiff.core.diff.boundary;

import de.brokenpipe.bpmndiff.core.diff.control.DiffEngine;
import de.brokenpipe.bpmndiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.io.File;

public class DiffCommand {

	private BpmnModelInstance from;
	private BpmnModelInstance to;

	public DiffCommand(final File from, final File to) {
		this.from = Bpmn.readModelFromFile(from);
		this.to = Bpmn.readModelFromFile(to);
	}

	public ChangeSet execute() {
		return new DiffEngine(this.from, this.to).compareDocuments();
	}
}
