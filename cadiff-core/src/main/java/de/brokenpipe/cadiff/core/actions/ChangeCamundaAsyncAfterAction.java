package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericChangePropertyPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

public record ChangeCamundaAsyncAfterAction(String id, Boolean oldValue, Boolean newValue) implements ChangePropertyAction<Boolean> {

	@Override
	public Patcher patcher() {
		return new GenericChangePropertyPatcher<>(this, FlowNode.class, FlowNode::isCamundaAsyncAfter, FlowNode::setCamundaAsyncAfter);
	}
}
