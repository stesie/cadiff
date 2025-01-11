package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeLoopCharacteristicsAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

@RequiredArgsConstructor
public class ChangeLoopCharacteristicsPatcher extends AbstractPatcher implements Patcher {

	final ChangeLoopCharacteristicsAction action;

	@Override
	public void accept(final PatcherContext context) {
		final Activity targetElement = findTargetWithType(context, action.id(), Activity.class);

		if (Boolean.FALSE.equals(action.newValue())) {
			targetElement.removeChildElement(targetElement.getLoopCharacteristics());
		}

		if (Boolean.TRUE.equals(action.newValue()) && targetElement.getLoopCharacteristics() == null) {
			targetElement.setLoopCharacteristics(context.getModelInstance().newInstance(
					MultiInstanceLoopCharacteristics.class));
		}
	}
}
