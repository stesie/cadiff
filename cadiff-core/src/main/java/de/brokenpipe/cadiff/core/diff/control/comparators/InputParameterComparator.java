package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeInputParameterAction;
import de.brokenpipe.cadiff.core.diff.control.AbstractSimpleWalker;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InputParameterComparator implements Comparator {
	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		final List<CamundaInputOutput> fromIOs = Optional.ofNullable(compareContext.from().getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaInputOutput.class).list())
				.orElse(Collections.emptyList());
		final List<CamundaInputOutput> toIOs = Optional.ofNullable(compareContext.to().getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaInputOutput.class).list())
				.orElse(Collections.emptyList());

		if (fromIOs.isEmpty() && toIOs.isEmpty()) {
			return Stream.empty();
		}

		if (fromIOs.size() > 1 || toIOs.size() > 1) {
			throw new NotImplementedException("can there be more than one camunda:InputOutput element?");
		}

		final List<CamundaInputParameter> fromInputs = fromIOs.stream()
				.flatMap(x -> x.getCamundaInputParameters().stream()).toList();
		final List<CamundaInputParameter> toInputs = toIOs.stream()
				.flatMap(x -> x.getCamundaInputParameters().stream()).toList();

		return new AbstractSimpleWalker<>(fromInputs, toInputs) {
			@Override
			protected String extractId(final CamundaInputParameter element) {
				return element.getCamundaName();
			}

			@Override
			protected Stream<Action> handleAdded(final CamundaInputParameter added) {
				return Stream.of(new ChangeInputParameterAction(compareContext.to().getId(), added.getCamundaName(), null,
						Value.of(added)));
			}

			@Override
			protected Stream<Action> handleUpdated(final CamundaInputParameter ipFrom,
					final CamundaInputParameter ipTo) {
				final var valueFrom = Value.of(ipFrom);
				final var valueTo = Value.of(ipTo);

				if (valueFrom.equals(valueTo)) {
					return Stream.empty();
				}

				return Stream.of(new ChangeInputParameterAction(compareContext.to().getId(), ipFrom.getCamundaName(),
						valueFrom, valueTo));
			}

			@Override
			protected Stream<Action> handleRemoved(final CamundaInputParameter removed) {
				return Stream.of(
						new ChangeInputParameterAction(compareContext.to().getId(), removed.getCamundaName(), Value.of(removed),
								null));
			}
		}.walk();

	}

}
