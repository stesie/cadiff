package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.DeleteExecutionListenerAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.PatchNotApplicableException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

import java.util.Objects;

@RequiredArgsConstructor
public class DeleteExecutionListenerPatcher extends AbstractPatcher implements Patcher {

	private final DeleteExecutionListenerAction action;

	@Override
	public void accept(final PatcherContext context) {
		final var baseEl = findTargetWithType(context, action.id(), BaseElement.class);

		final var existingListener = baseEl.getExtensionElements().getElementsQuery()
				.filterByType(CamundaExecutionListener.class).list().stream()
				.filter(this::extensionListenerEquals)
				.findFirst();

		if (existingListener.isEmpty()) {
			throw new PatchNotApplicableException("Listener not found");
		}

		baseEl.getExtensionElements().getDomElement().removeChild(existingListener.get().getDomElement());
	}

	private boolean extensionListenerEquals(final CamundaExecutionListener a) {
		return Objects.equals(a.getCamundaEvent(), action.camundaEvent())
				&& Objects.equals(a.getCamundaClass(), action.camundaClass())
				&& Objects.equals(a.getCamundaDelegateExpression(), action.camundaDelegateExpression())
				&& Objects.equals(a.getCamundaExpression(), action.camundaExpression());
	}
}
