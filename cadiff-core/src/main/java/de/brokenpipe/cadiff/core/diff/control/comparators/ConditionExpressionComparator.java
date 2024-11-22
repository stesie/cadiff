package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeConditionExpressionAction;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Optional;
import java.util.stream.Stream;

public class ConditionExpressionComparator extends UpcastComparator<SequenceFlow>
		implements StringPropertyComparator<SequenceFlow> {

	@Override
	protected Class<SequenceFlow> getClassType() {
		return SequenceFlow.class;
	}

	@Override
	protected Stream<Action> compare(final SequenceFlow from, final SequenceFlow to) {
		return compareStringProperty(
				x -> Optional.ofNullable(x.getConditionExpression()).map(ModelElementInstance::getTextContent)
						.orElse(null),
				ChangeConditionExpressionAction.class, from, to);
	}
}
