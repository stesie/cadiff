package de.brokenpipe.cadiff.core.patch.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatcherContext {

	private final BpmnModelInstance modelInstance;
	private final BaseElement process;

	public static PatcherContext of(final BpmnModelInstance modelInstance) {
		return new PatcherContext(modelInstance, null);
	}

	public PatcherContext withProcess(final BaseElement process) {
		return new PatcherContext(this.modelInstance, process);
	}
}
