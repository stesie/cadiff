package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAllAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaOut;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareProperty;

public class OutMappingAllComparator implements Comparator {

	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		return compareProperty(el -> mapInAllToConfig(extractCamundaOuts(el)),
				ChangeOutMappingAllAction::new, compareContext);
	}

	private static List<CamundaOut> extractCamundaOuts(final BaseElement el) {
		return Optional.ofNullable(el.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaOut.class).list())
				.orElse(Collections.emptyList());
	}

	private static ChangeOutMappingAllAction.Config mapInAllToConfig(final List<CamundaOut> in) {
		return in.stream()
				.filter(x -> "all".equals(x.getCamundaVariables()))
				.findFirst()
				.map(camundaIn -> new ChangeOutMappingAllAction.Config(true,
						Boolean.valueOf(camundaIn.getCamundaLocal())))
				.orElseGet(ChangeOutMappingAllAction.Config::disabled);
	}

}
