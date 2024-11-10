package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@RequiredArgsConstructor
public class AddSimpleFlowNodePatcher extends AbstractPatcher implements Patcher {

	private final AddSimpleFlowNodeAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		addFlowElement(bpmnModelInstance, action.id(), action.elementTypeName(), action.bounds());

	}

}
