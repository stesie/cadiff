package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeNameAction;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

public class ChangeNamePatcher extends AbstractChangePropertyPatcher<FlowElement, String> {

	public ChangeNamePatcher(final ChangeNameAction action) {
		super(action, FlowElement.class, FlowElement::getName, FlowElement::setName);
	}
}
