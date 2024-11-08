package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.Action;
import de.brokenpipe.bpmndiff.core.actions.ChangeCamundaAsyncAfterAction;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.stream.Stream;

public class CamundaAsyncAfterComparator extends UpcastComperator<FlowNode>
		implements PropertyComparator<FlowNode, Boolean> {

	@Override
	protected Class<FlowNode> getClassType() {
		return FlowNode.class;
	}

	@Override
	protected Stream<Action> compare(final FlowNode from, final FlowNode to) {
		return compareProperty(FlowNode::isCamundaAsyncAfter, Boolean.class,
				ChangeCamundaAsyncAfterAction.class, from, to);
	}
}
