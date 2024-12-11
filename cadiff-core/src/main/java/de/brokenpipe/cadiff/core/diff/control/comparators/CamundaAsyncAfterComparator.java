package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeCamundaAsyncAfterAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.stream.Stream;

public class CamundaAsyncAfterComparator extends UpcastComparator<FlowNode>
		implements PropertyComparator<FlowNode, Boolean> {

	@Override
	protected Class<FlowNode> getClassType() {
		return FlowNode.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<FlowNode> compareContext) {
		return compareProperty(FlowNode::isCamundaAsyncAfter, Boolean.class,
				ChangeCamundaAsyncAfterAction.class, compareContext);
	}
}
