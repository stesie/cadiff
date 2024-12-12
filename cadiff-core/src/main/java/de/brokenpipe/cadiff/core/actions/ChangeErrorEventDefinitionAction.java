package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeEventDefinitionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.Error;
import org.camunda.bpm.model.bpmn.instance.ErrorEventDefinition;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.BPMN_ATTRIBUTE_ERROR_REF;

public record ChangeErrorEventDefinitionAction(String id, String errorDefinitionId, String oldErrorRef,
											   String newErrorRef) implements Action, ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeEventDefinitionPatcher<>(id, errorDefinitionId, oldErrorRef, newErrorRef,
				ErrorEventDefinition.class, Error.class, ErrorEventDefinition::getError, ErrorEventDefinition::setError,
				BPMN_ATTRIBUTE_ERROR_REF);
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	@JsonIgnore
	public String oldValue() {
		return oldErrorRef;
	}

	@Override
	@JsonIgnore
	public String newValue() {
		return newErrorRef;
	}

	@Override
	public String attributeName() {
		return "errorRef";
	}
}
