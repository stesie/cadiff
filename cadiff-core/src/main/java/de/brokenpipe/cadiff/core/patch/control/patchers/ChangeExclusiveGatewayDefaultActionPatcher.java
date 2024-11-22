package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeExclusiveGatewayDefaultAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeExclusiveGatewayDefaultActionPatcher extends AbstractPatcher implements Patcher {
	
	private final ChangeExclusiveGatewayDefaultAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExclusiveGateway targetElement = findTargetWithType(context, action.id(), ExclusiveGateway.class);

		final var oldValue = Optional.ofNullable(targetElement.getDefault()).map(BaseElement::getId).orElse(null);
		
		if (action.oldValue() == null
				? oldValue != null
				: !action.oldValue().equals(oldValue)) {
			throw new ValueMismatchException(action.id(), oldValue, action.oldValue());
		}

		final SequenceFlow newTarget = findTargetWithType(context, action.newValue(), SequenceFlow.class);
		targetElement.setDefault(newTarget);
	}
}
