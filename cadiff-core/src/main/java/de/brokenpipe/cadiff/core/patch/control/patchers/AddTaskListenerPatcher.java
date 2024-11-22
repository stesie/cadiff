package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddTaskListenerAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaTaskListener;

@RequiredArgsConstructor
public class AddTaskListenerPatcher extends AbstractPatcher implements Patcher {

	private final AddTaskListenerAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());

		final var ext = extEl.addExtensionElement(CamundaTaskListener.class);
		ext.setCamundaEvent(action.camundaEvent());
		ext.setCamundaClass(action.camundaClass());
		ext.setCamundaDelegateExpression(action.camundaDelegateExpression());
		ext.setCamundaExpression(action.camundaExpression());
	}
}
