package de.brokenpipe.cadiff.core.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeEventDefinitionPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.Signal;
import org.camunda.bpm.model.bpmn.instance.SignalEventDefinition;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.BPMN_ATTRIBUTE_SIGNAL_REF;

public record ChangeSignalEventDefinitionAction(String id, String signalDefinitionId, String oldSignalRef,
												String newSignalRef) implements Action, ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new ChangeEventDefinitionPatcher<>(id, signalDefinitionId, oldSignalRef, newSignalRef,
				SignalEventDefinition.class, Signal.class, SignalEventDefinition::getSignal,
				SignalEventDefinition::setSignal, BPMN_ATTRIBUTE_SIGNAL_REF);
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	@JsonIgnore
	public String oldValue() {
		return oldSignalRef;
	}

	@Override
	@JsonIgnore
	public String newValue() {
		return newSignalRef;
	}

	@Override
	public String attributeName() {
		return "signalRef";
	}
}
