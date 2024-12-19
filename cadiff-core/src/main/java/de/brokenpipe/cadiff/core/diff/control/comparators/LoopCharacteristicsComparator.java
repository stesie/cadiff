package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeLoopCharacteristicsAction;
import de.brokenpipe.cadiff.core.actions.ChangeLoopCharacteristicsIsSequentialAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LoopCharacteristicsComparator extends UpcastComparator<CallActivity> {
	@Override
	protected Class<CallActivity> getClassType() {
		return CallActivity.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<CallActivity> compareContext) {
		if (!(compareContext.to().getLoopCharacteristics() instanceof final MultiInstanceLoopCharacteristics toConfig)) {
			if (compareContext.from().getLoopCharacteristics() != null) {
				// removed
				return Stream.of(new ChangeLoopCharacteristicsAction(compareContext.to().getId(), Boolean.TRUE, Boolean.FALSE));
			}

			// already off, no change
			return Stream.empty();
		}

		final List<Action> actions = new ArrayList<>();

		if (compareContext.from().getLoopCharacteristics() == null) {
			// added
			final var activate = new ChangeLoopCharacteristicsAction(compareContext.to().getId(),
					Boolean.FALSE, Boolean.TRUE);
			activate.patcher().accept(PatcherContext.of(compareContext.fromInstance(), compareContext.fromContainer()));
			actions.add(activate);
		}

		if (!(compareContext.from().getLoopCharacteristics() instanceof final MultiInstanceLoopCharacteristics fromConfig)) {
			throw new IllegalStateException("LoopCharacteristics is not a MultiInstanceLoopCharacteristics");
		}

		if (fromConfig.isSequential() != toConfig.isSequential()) {
			actions.add(new ChangeLoopCharacteristicsIsSequentialAction(compareContext.to().getId(),
					Boolean.valueOf(fromConfig.isSequential()), Boolean.valueOf(toConfig.isSequential())));
		}

			// toConfig.isSequential()
		// compareContext.from().getLoopCharacteristics()
		return actions.stream();
	}
}
