package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeInMappingAllAction;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InMappingAllComparator
		implements Comparator, PropertyComparator<BaseElement, ChangeInMappingAllAction.Config> {

	@Override
	public Stream<Action> apply(final BaseElement from, final BaseElement to) {
		return compareProperty(el -> mapInAllToConfig(extractCamundaIns(el)),
				ChangeInMappingAllAction::new,
				from, to);
	}

	private static List<CamundaIn> extractCamundaIns(final BaseElement el) {
		return Optional.ofNullable(el.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaIn.class).list())
				.orElse(Collections.emptyList());
	}

	private static ChangeInMappingAllAction.Config mapInAllToConfig(final List<CamundaIn> in) {
		return in.stream()
				.filter(x -> "all".equals(x.getCamundaVariables()))
				.findFirst()
				.map(camundaIn -> new ChangeInMappingAllAction.Config(Boolean.TRUE,
						Boolean.valueOf(camundaIn.getCamundaLocal())))
				.orElseGet(ChangeInMappingAllAction.Config::disabled);
	}

}
