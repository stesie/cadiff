package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeConditionExpressionAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Optional;
import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareStringProperty;

public class ConditionExpressionComparator extends UpcastComparator<SequenceFlow> {

	@Override
	protected Class<SequenceFlow> getClassType() {
		return SequenceFlow.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<SequenceFlow> compareContext) {
		return compareStringProperty(
				x -> Optional.ofNullable(x.getConditionExpression()).map(ModelElementInstance::getTextContent)
						.orElse(null),
				ChangeConditionExpressionAction.class, compareContext);
	}
}
