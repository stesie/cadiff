package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaAsyncBeforeAction;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.stream.Stream;

public class CamundaAsyncBeforeComparator extends UpcastComparator<FlowNode>
		implements PropertyComparator<FlowNode, Boolean> {

	@Override
	protected Class<FlowNode> getClassType() {
		return FlowNode.class;
	}

	@Override
	protected Stream<Action> compare(final FlowNode from, final FlowNode to) {
		return compareProperty(FlowNode::isCamundaAsyncBefore, Boolean.class,
				ChangeCamundaAsyncBeforeAction.class, from, to);
	}
}
