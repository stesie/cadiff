package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeInMappingAction;
import de.brokenpipe.cadiff.core.diff.control.AbstractSimpleWalker;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InMappingComparator implements Comparator {
	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		final CompareContext<List<CamundaIn>> z = compareContext.map(InMappingComparator::extractCamundaIns);
		
		return new AbstractSimpleWalker<>(z.from(), z.to()) {
			@Override
			protected String extractKey(final CamundaIn element) {
				return element.getCamundaTarget();
			}

			@Override
			protected Stream<Action> handleAdded(final CamundaIn added) {
				return Stream.of(new ChangeInMappingAction(compareContext.to().getId(), added.getCamundaTarget(), null,
						ChangeInMappingAction.Config.from(added)));
			}

			@Override
			protected Stream<Action> handleUpdated(final CamundaIn from, final CamundaIn to) {
				final var oldConfig = ChangeInMappingAction.Config.from(from);
				final var newConfig = ChangeInMappingAction.Config.from(to);

				if (oldConfig.equals(newConfig)) {
					return Stream.empty();
				}

				return Stream.of(new ChangeInMappingAction(compareContext.to().getId(), from.getCamundaTarget(),
						oldConfig, newConfig));
			}

			@Override
			protected Stream<Action> handleRemoved(final CamundaIn removed) {
				return Stream.of(new ChangeInMappingAction(compareContext.to().getId(), removed.getCamundaTarget(),
						ChangeInMappingAction.Config.from(removed), null));
			}
		}.walk();

	}

	private static List<CamundaIn> extractCamundaIns(final BaseElement el) {
		return Optional.ofNullable(el.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaIn.class).list())
				.orElse(Collections.emptyList())
				.stream()
				.filter(x -> !"all".equals(x.getCamundaVariables()))
				.filter(x -> x.getCamundaBusinessKey() == null)
				.toList();
	}

}
