package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaAsyncBeforeAction;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

public class ChangeCamundaAsyncBeforePatcher extends AbstractChangePropertyPatcher<FlowNode, Boolean> {

	public ChangeCamundaAsyncBeforePatcher(final ChangeCamundaAsyncBeforeAction action) {
		super(action, FlowNode.class, FlowNode::isCamundaAsyncBefore, FlowNode::setCamundaAsyncBefore);
	}
}
