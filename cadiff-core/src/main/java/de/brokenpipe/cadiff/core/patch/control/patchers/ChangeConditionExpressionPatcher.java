package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeConditionExpressionAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeConditionExpressionPatcher extends AbstractPatcher implements Patcher {

	private final ChangeConditionExpressionAction action;

	@Override
	public void accept(final PatcherContext context) {
		final SequenceFlow targetElement = findTargetWithType(context, action.id(), SequenceFlow.class);

		final var oldValue = Optional.ofNullable(targetElement.getConditionExpression())
				.map(BaseElement::getTextContent).orElse(null);

		if (action.oldValue() == null
				? oldValue != null
				: !action.oldValue().equals(oldValue)) {
			throw new ValueMismatchException(action.id(), oldValue, action.oldValue());
		}

		if (action.newValue() == null) {
			targetElement.removeConditionExpression();
		}

		final var conditionExpression = context.getModelInstance().newInstance(ConditionExpression.class);
		conditionExpression.getDomElement().removeAttribute("id");
		conditionExpression.setType("bpmn:tFormalExpression");
		conditionExpression.setTextContent(action.newValue());

		targetElement.setConditionExpression(conditionExpression);
	}
}
