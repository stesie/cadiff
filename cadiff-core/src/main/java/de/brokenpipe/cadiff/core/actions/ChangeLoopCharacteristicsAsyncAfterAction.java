package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericLoopCharacteristicsPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

public record ChangeLoopCharacteristicsAsyncAfterAction(String id, Boolean oldValue, Boolean newValue) implements ChangePropertyAction<Boolean> {

	@Override
	public Patcher patcher() {
		return new GenericLoopCharacteristicsPatcher<>(this,
				MultiInstanceLoopCharacteristics::isCamundaAsyncAfter,
				MultiInstanceLoopCharacteristics::setCamundaAsyncAfter);
	}

	@Override
	public String attributeName() {
		return "[multi] asyncAfter";
	}

}
