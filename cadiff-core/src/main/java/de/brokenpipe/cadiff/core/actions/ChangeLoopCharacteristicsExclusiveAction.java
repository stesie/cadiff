package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericLoopCharacteristicsPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

public record ChangeLoopCharacteristicsExclusiveAction(String id, Boolean oldValue, Boolean newValue) implements ChangePropertyAction<Boolean> {

	@Override
	public Patcher patcher() {
		return new GenericLoopCharacteristicsPatcher<>(this,
				MultiInstanceLoopCharacteristics::isCamundaExclusive,
				MultiInstanceLoopCharacteristics::setCamundaExclusive);
	}

	@Override
	public String attributeName() {
		return "[multi] exclusive";
	}

}
