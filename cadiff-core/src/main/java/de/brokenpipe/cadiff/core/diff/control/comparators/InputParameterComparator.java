package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeInputParameterAction;
import de.brokenpipe.cadiff.core.diff.control.AbstractSimpleWalker;
import de.brokenpipe.cadiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Comparator for execution listeners. We don't try to identify updates. Just compare from and to lists and create add
 * and remove actions as needed.
 */
public class InputParameterComparator implements Comparator {
	@Override
	public Stream<Action> apply(final BaseElement from, final BaseElement to) {
		final List<CamundaInputOutput> fromIOs = Optional.ofNullable(from.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaInputOutput.class).list())
				.orElse(Collections.emptyList());
		final List<CamundaInputOutput> toIOs = Optional.ofNullable(to.getExtensionElements())
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
				return Stream.of(new ChangeInputParameterAction(to.getId(), added.getCamundaName(), null,
						Value.of(added)));
			}

			@Override
			protected Stream<Action> handleUpdated(final CamundaInputParameter ipFrom,
					final CamundaInputParameter ipTo) {
				return ipFrom.getTextContent().equals(ipTo.getTextContent())
						? Stream.empty()
						: Stream.of(new ChangeInputParameterAction(to.getId(), ipFrom.getCamundaName(),
								Value.of(ipFrom), Value.of(ipTo)));
			}

			@Override
			protected Stream<Action> handleRemoved(final CamundaInputParameter removed) {
				return Stream.of(
						new ChangeInputParameterAction(to.getId(), removed.getCamundaName(), Value.of(removed),
								null));
			}
		}.walk();

	}

}
