package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericLoopCharacteristicsPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

public record ChangeLoopCharacteristicsIsSequentialAction(String id, Boolean oldValue, Boolean newValue) implements ChangePropertyAction<Boolean> {

	@Override
	public Patcher patcher() {
		return new GenericLoopCharacteristicsPatcher<Boolean>(this, MultiInstanceLoopCharacteristics::isSequential, MultiInstanceLoopCharacteristics::setSequential);
	}
}
