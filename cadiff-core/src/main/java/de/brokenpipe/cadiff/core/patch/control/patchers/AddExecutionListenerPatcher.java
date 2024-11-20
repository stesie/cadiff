package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

import java.util.Optional;

@RequiredArgsConstructor
public class AddExecutionListenerPatcher extends AbstractPatcher implements Patcher {

	private final AddExecutionListenerAction action;

	@Override
	public void accept(final PatcherContext context) {
		final var baseEl = findTargetWithType(context, action.id(), BaseElement.class);

		final ExtensionElements extEl = Optional.ofNullable(baseEl.getExtensionElements())
				.orElseGet(() -> {
					final var newExtEl = context.getModelInstance().newInstance(ExtensionElements.class);
					baseEl.setExtensionElements(newExtEl);
					return newExtEl;
				});

		final var ext = extEl.addExtensionElement(CamundaExecutionListener.class);
		ext.setCamundaEvent(action.camundaEvent());
		ext.setCamundaClass(action.camundaClass());
		ext.setCamundaDelegateExpression(action.camundaDelegateExpression());
		ext.setCamundaExpression(action.camundaExpression());
	}
}
