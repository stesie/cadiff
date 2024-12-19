package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.GenericLoopCharacteristicsPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

public record ChangeLoopCharacteristicsCollectionAction(String id, String oldValue, String newValue) implements ChangePropertyAction<String> {

	@Override
	public Patcher patcher() {
		return new GenericLoopCharacteristicsPatcher<>(this,
				MultiInstanceLoopCharacteristics::getCamundaCollection,
				MultiInstanceLoopCharacteristics::setCamundaCollection);
	}
}
