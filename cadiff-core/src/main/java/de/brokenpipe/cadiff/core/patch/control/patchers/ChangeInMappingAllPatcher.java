package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeInMappingAllAction;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.CAMUNDA_NS;

@RequiredArgsConstructor
public class ChangeInMappingAllPatcher extends AbstractPatcher implements Patcher {

	private final ChangeInMappingAllAction action;

	@Override
	public void accept(final PatcherContext context) {
		final var extEl = findExtensionElements(context, action.id());

		final var existingIMA = extEl.getElementsQuery().filterByType(CamundaIn.class).list().stream()
				.filter(x -> "all".equals(x.getCamundaVariables()))
				.findFirst();

		if (!action.newValue().enabled()) {
			existingIMA.ifPresent(extEl::removeChildElement);
			return;
		}

		final var ima = existingIMA.orElseGet(() -> extEl.addExtensionElement(CamundaIn.class));
		ima.setCamundaVariables("all");

		if (action.newValue().local() == Boolean.TRUE) {
			ima.setCamundaLocal(true);
		} else {
			// we don't set it false, to be compatible with the style Camunda Modeller writes
			ima.removeAttributeNs( CAMUNDA_NS, "local");
		}
	}
}
