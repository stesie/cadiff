package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.diff.control.StreamUtils;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.List;
import java.util.Optional;
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

		Optional<Action> activate = Optional.empty();

		if (compareContext.from().getLoopCharacteristics() == null) {
			// added
			activate = Optional.of(new ChangeLoopCharacteristicsAction(compareContext.to().getId(),
					Boolean.FALSE, Boolean.TRUE));
			activate.get().patcher().accept(PatcherContext.of(compareContext.fromInstance(), compareContext.fromContainer()));
		}

		final CompareContext<MultiInstanceLoopCharacteristics> milcCompareContext = compareContext
				.mapValue(Activity::getLoopCharacteristics)
				.mapValue(MultiInstanceLoopCharacteristics.class::cast);

		return StreamUtils.mergeStreams(List.of(
				activate.stream(),

				PropertyComparator.compareProperty(MultiInstanceLoopCharacteristics::isSequential,
						Boolean.class, ChangeLoopCharacteristicsIsSequentialAction.class, milcCompareContext),

				PropertyComparator.compareProperty(
						x -> Optional.ofNullable(x.getLoopCardinality())
								.map(ModelElementInstance::getTextContent).orElse(null),
						String.class, ChangeLoopCharacteristicsCardinalityAction.class, milcCompareContext),

				PropertyComparator.compareProperty(MultiInstanceLoopCharacteristics::getCamundaCollection,
						String.class, ChangeLoopCharacteristicsCollectionAction.class, milcCompareContext),

				PropertyComparator.compareProperty(MultiInstanceLoopCharacteristics::getCamundaElementVariable,
						String.class, ChangeLoopCharacteristicsElementVariableAction.class, milcCompareContext)

		));

	}

}
