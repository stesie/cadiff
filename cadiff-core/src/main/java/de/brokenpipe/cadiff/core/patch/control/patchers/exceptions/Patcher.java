package de.brokenpipe.cadiff.core.patch.control.patchers.exceptions;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.function.Consumer;

public interface Patcher extends Consumer<BpmnModelInstance> {
}
