package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaExclusiveAction;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

public class ChangeCamundaExclusivePatcher extends AbstractChangePropertyPatcher<FlowNode, Boolean> {

	public ChangeCamundaExclusivePatcher(final ChangeCamundaExclusiveAction action) {
		super(action, FlowNode.class, FlowNode::isCamundaExclusive, FlowNode::setCamundaExclusive);
	}
}
