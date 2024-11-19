package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

@RequiredArgsConstructor
public class AddExecutionListenerPatcher extends AbstractPatcher implements Patcher {

	private final AddExecutionListenerAction action;

	@Override
	public void accept(final PatcherContext context) {
		final var baseEl = findTargetWithType(context, action.id(), BaseElement.class);

		final var ext = baseEl.getExtensionElements().addExtensionElement(CamundaExecutionListener.class);
		ext.setCamundaEvent(action.camundaEvent());
		ext.setCamundaClass(action.camundaClass());
		ext.setCamundaDelegateExpression(action.camundaDelegateExpression());
		ext.setCamundaExpression(action.camundaExpression());
	}
}
