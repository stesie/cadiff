package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaAsyncAfterAction;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

public class ChangeCamundaAsyncAfterPatcher extends AbstractChangePropertyPatcher<FlowNode, Boolean> {

	public ChangeCamundaAsyncAfterPatcher(final ChangeCamundaAsyncAfterAction action) {
		super(action, FlowNode.class, FlowNode::isCamundaAsyncAfter, FlowNode::setCamundaAsyncAfter);
	}
}
