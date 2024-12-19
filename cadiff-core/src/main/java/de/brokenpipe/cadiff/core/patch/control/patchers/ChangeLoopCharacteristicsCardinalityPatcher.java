package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeLoopCharacteristicsCardinalityAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.LoopCardinality;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeLoopCharacteristicsCardinalityPatcher extends AbstractPatcher implements Patcher {

	final ChangeLoopCharacteristicsCardinalityAction action;

	@Override
	public void accept(final PatcherContext context) {
		final CallActivity targetElement = findTargetWithType(context, action.id(), CallActivity.class);

		if (!(targetElement.getLoopCharacteristics() instanceof final MultiInstanceLoopCharacteristics milc)) {
			throw new IllegalStateException("LoopCharacteristics is not a MultiInstanceLoopCharacteristics");
		}

		final var oldValueActual = Optional.ofNullable(milc.getLoopCardinality())
				.map(ModelElementInstance::getTextContent).orElse(null);

		if (!Objects.equals(oldValueActual, action.oldValue())) {
			throw new ValueMismatchException(action.id(), oldValueActual, action.oldValue());
		}

		if (action.newValue() == null) {
			milc.removeChildElement(milc.getLoopCardinality());
			return;
		}

		Optional.ofNullable(milc.getLoopCardinality())
				.orElseGet(() -> {
					final var newLoopCardinality = context.getModelInstance().newInstance(LoopCardinality.class);
					newLoopCardinality.getDomElement().removeAttribute("id");
					milc.setLoopCardinality(newLoopCardinality);
					return newLoopCardinality;
				})
				.setTextContent(action.newValue());
	}
}
