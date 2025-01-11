package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangePropertyAction;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericLoopCharacteristicsPatcher<ValueT>
		extends GenericChangePropertyPatcher<Activity, ValueT> implements Patcher {

	public GenericLoopCharacteristicsPatcher(final ChangePropertyAction<ValueT> action,
			final Function<MultiInstanceLoopCharacteristics, ValueT> getter,
			final BiConsumer<MultiInstanceLoopCharacteristics, ValueT> setter) {
		super(action, Activity.class,
				activity -> {
					final var lc = activity.getLoopCharacteristics();
					if (!(lc instanceof final MultiInstanceLoopCharacteristics milc)) {
						throw new IllegalStateException("LoopCharacteristics is not a MultiInstanceLoopCharacteristics");
					}
					return getter.apply(milc);
				},
				(activity, value) -> {
					final var lc = activity.getLoopCharacteristics();
					if (!(lc instanceof final MultiInstanceLoopCharacteristics milc)) {
						throw new IllegalStateException("LoopCharacteristics is not a MultiInstanceLoopCharacteristics");
					}
					setter.accept(milc, value);
				});
	}

}
