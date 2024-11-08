package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaExclusiveAction;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.stream.Stream;

public class CamundaExclusiveComparator extends UpcastComperator<FlowNode>
		implements PropertyComparator<FlowNode, Boolean> {

	@Override
	protected Class<FlowNode> getClassType() {
		return FlowNode.class;
	}

	@Override
	protected Stream<Action> compare(final FlowNode from, final FlowNode to) {
		return compareProperty(FlowNode::isCamundaExclusive, Boolean.class,
				ChangeCamundaExclusiveAction.class, from, to);
	}
}
