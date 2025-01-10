package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAction;
import de.brokenpipe.cadiff.core.diff.control.AbstractSimpleWalker;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaOut;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class OutMappingComparator implements Comparator {
	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		final CompareContext<List<CamundaOut>> z = compareContext.map(OutMappingComparator::extractCamundaOuts);
		
		return new AbstractSimpleWalker<>(z.from(), z.to()) {
			@Override
			protected String extractKey(final CamundaOut element) {
				return element.getCamundaTarget();
			}

			@Override
			protected Stream<Action> handleAdded(final CamundaOut added) {
				return Stream.of(new ChangeOutMappingAction(compareContext.to().getId(), added.getCamundaTarget(), null,
						ChangeOutMappingAction.Config.from(added)));
			}

			@Override
			protected Stream<Action> handleUpdated(final CamundaOut from, final CamundaOut to) {
				final var oldConfig = ChangeOutMappingAction.Config.from(from);
				final var newConfig = ChangeOutMappingAction.Config.from(to);

				if (oldConfig.equals(newConfig)) {
					return Stream.empty();
				}

				return Stream.of(new ChangeOutMappingAction(compareContext.to().getId(), from.getCamundaTarget(),
						oldConfig, newConfig));
			}

			@Override
			protected Stream<Action> handleRemoved(final CamundaOut removed) {
				return Stream.of(new ChangeOutMappingAction(compareContext.to().getId(), removed.getCamundaTarget(),
						ChangeOutMappingAction.Config.from(removed), null));
			}
		}.walk();

	}

	private static List<CamundaOut> extractCamundaOuts(final BaseElement el) {
		return Optional.ofNullable(el.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaOut.class).list())
				.orElse(Collections.emptyList())
				.stream()
				.filter(x -> !"all".equals(x.getCamundaVariables()))
				.toList();
	}

}
