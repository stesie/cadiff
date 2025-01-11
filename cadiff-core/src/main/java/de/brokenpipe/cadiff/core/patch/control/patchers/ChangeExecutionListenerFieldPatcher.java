package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeExecutionListenerFieldAction;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExpression;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaField;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaString;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeExecutionListenerFieldPatcher extends AbstractPatcher implements Patcher {

	private final ChangeExecutionListenerFieldAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());
		final CamundaExecutionListener listener = findExecutionListener(extEl, action.key());

		final Optional<CamundaField> existingField = listener.getCamundaFields().stream()
				.filter(x -> Objects.equals(action.fieldName(), x.getCamundaName()))
				.findFirst();

		if (action.oldValue() == null) {
			if (existingField.isEmpty()) {
				// field doesn't exist, just create a new one
				final var field = context.getModelInstance().newInstance(CamundaField.class);
				field.removeAttribute("id");
				field.setCamundaName(action.fieldName());
				listener.addChildElement(field);

				updateFromAction(field);
				return;
			}

			if (action.newValue().equals(ChangeExecutionListenerFieldAction.Config.from(existingField.get()))) {
				// same field already exists -> silently ignore
				return;
			}

			throw new NotImplementedException("field already exists, but is different");
		}

		if (existingField.isEmpty()) {
			// FIXME provide custom exception and provide fieldName
			throw new TargetElementNotFoundException(action.id());
		}

		if (action.newValue() == null) {
			listener.removeChildElement(existingField.get());
			return;
		}

		updateFromAction(existingField.get());
	}

	private void updateFromAction(final CamundaField field) {
		if (action.newValue().isSourceExpression()) {
			final CamundaExpression expression = field.getModelInstance().newInstance(CamundaExpression.class);
			expression.removeAttribute("id");
			expression.setTextContent(action.newValue().source());
			field.setCamundaExpressionChild(expression);

			Optional.ofNullable(field.getCamundaString()).ifPresent(field::removeChildElement);
		} else {
			final CamundaString string = field.getModelInstance().newInstance(CamundaString.class);
			string.removeAttribute("id");
			string.setTextContent(action.newValue().source());
			field.setCamundaString(string);

			Optional.ofNullable(field.getCamundaExpressionChild()).ifPresent(field::removeChildElement);
		}
	}
}
