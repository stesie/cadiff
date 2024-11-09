package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeIdAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

@RequiredArgsConstructor
public class ChangeIdPatcher implements Patcher {

	private final ChangeIdAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		final ModelElementInstance target = bpmnModelInstance.getModelElementById(action.oldId());

		if (target == null) {
			throw new TargetElementNotFoundException(action.oldId());
		}

		if (target instanceof final BaseElement baseElement) {
			baseElement.setId(action.newId());
			return;
		}

		throw new IllegalStateException("Target element is not a BaseElement");
	}
}
