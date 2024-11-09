package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

@RequiredArgsConstructor
public class DeleteElementPatcher implements Patcher {

	private final DeleteElementAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		final ModelElementInstance target = bpmnModelInstance.getModelElementById(action.id());

		if (target == null) {
			throw new TargetElementNotFoundException(action.id());
		}

		if (target instanceof final SequenceFlow sequenceFlow) {
			sequenceFlow.getDiagramElement().getParentElement().removeChildElement(sequenceFlow.getDiagramElement());
		}

		target.getParentElement().removeChildElement(target);
	}
}
