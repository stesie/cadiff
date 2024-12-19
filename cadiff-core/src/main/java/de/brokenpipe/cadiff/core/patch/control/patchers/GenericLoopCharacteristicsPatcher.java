package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericLoopCharacteristicsPatcher<ValueT>
		extends GenericChangePropertyPatcher<CallActivity, ValueT> implements Patcher {

	public GenericLoopCharacteristicsPatcher(final ChangePropertyAction<ValueT> action,
			final Function<MultiInstanceLoopCharacteristics, ValueT> getter,
			final BiConsumer<MultiInstanceLoopCharacteristics, ValueT> setter) {
		super(action, CallActivity.class,
				callActivity -> {
					final var lc = callActivity.getLoopCharacteristics();
					if (!(lc instanceof final MultiInstanceLoopCharacteristics milc)) {
						throw new IllegalStateException("LoopCharacteristics is not a MultiInstanceLoopCharacteristics");
					}
					return getter.apply(milc);
				},
				(callActivity, value) -> {
					final var lc = callActivity.getLoopCharacteristics();
					if (!(lc instanceof final MultiInstanceLoopCharacteristics milc)) {
						throw new IllegalStateException("LoopCharacteristics is not a MultiInstanceLoopCharacteristics");
					}
					setter.accept(milc, value);
				});
	}

}
