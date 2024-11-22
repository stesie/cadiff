package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeExclusiveGatewayDefaultAction;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;

import java.util.Optional;
import java.util.stream.Stream;

public class ExclusiveGatewayDefaultComparator extends UpcastComparator<ExclusiveGateway>
		implements StringPropertyComparator<ExclusiveGateway> {

	@Override
	protected Class<ExclusiveGateway> getClassType() {
		return ExclusiveGateway.class;
	}

	@Override
	protected Stream<Action> compare(final ExclusiveGateway from, final ExclusiveGateway to) {
		return compareStringProperty(x -> Optional.ofNullable(x.getDefault()).map(BaseElement::getId).orElse(null),
				ChangeExclusiveGatewayDefaultAction.class, from, to);
	}
}
