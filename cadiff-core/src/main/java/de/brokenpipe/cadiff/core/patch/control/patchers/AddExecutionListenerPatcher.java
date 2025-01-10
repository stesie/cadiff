package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

@RequiredArgsConstructor
public class AddExecutionListenerPatcher extends AbstractPatcher implements Patcher {

	private final AddExecutionListenerAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());

		final var ext = extEl.addExtensionElement(CamundaExecutionListener.class);
		ext.setCamundaEvent(action.key().camundaEvent());
		ext.setCamundaClass(action.key().camundaClass());
		ext.setCamundaDelegateExpression(action.key().camundaDelegateExpression());
		ext.setCamundaExpression(action.key().camundaExpression());
	}

}
