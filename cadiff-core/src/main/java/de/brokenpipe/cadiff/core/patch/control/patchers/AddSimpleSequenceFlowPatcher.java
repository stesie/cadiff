package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class AddSimpleSequenceFlowPatcher extends AbstractAddPatcher implements Patcher {

	private final AddSimpleSequenceFlowAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		addSequenceFlow(bpmnModelInstance, action.id(), action.sourceId(), action.targetId(), action.waypoints());
	}

}
