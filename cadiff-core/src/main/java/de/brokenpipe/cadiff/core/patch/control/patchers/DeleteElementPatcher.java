package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.DeleteElementAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

@RequiredArgsConstructor
public class DeleteElementPatcher extends AbstractPatcher implements Patcher {

	private final DeleteElementAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		deleteElement(bpmnModelInstance, action.id());
	}
}
