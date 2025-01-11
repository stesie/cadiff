package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.DeleteExecutionListenerAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

@RequiredArgsConstructor
public class DeleteExecutionListenerPatcher extends AbstractPatcher implements Patcher {

	private final DeleteExecutionListenerAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());
		final CamundaExecutionListener existingListener = findExecutionListener(extEl, action.key());

		extEl.getDomElement().removeChild(existingListener.getDomElement());
	}

}
