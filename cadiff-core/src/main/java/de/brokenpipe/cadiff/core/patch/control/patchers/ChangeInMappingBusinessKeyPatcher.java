package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaBusinessKeyAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;

@RequiredArgsConstructor
public class ChangeInMappingBusinessKeyPatcher extends AbstractPatcher implements Patcher {

	private final ChangeCamundaBusinessKeyAction action;

	@Override
	public void accept(final PatcherContext context) {
		final var extEl = findExtensionElements(context, action.id());

		final var existingIMBK = extEl.getElementsQuery().filterByType(CamundaIn.class).list().stream()
				.filter(x -> x.getCamundaBusinessKey() != null)
				.findFirst();

		if (action.newValue() == null) {
			existingIMBK.ifPresent(extEl::removeChildElement);
			return;
		}

		if (existingIMBK.isPresent() && action.oldValue() != null && !action.oldValue().equals(existingIMBK.get().getCamundaBusinessKey())) {
			throw new ValueMismatchException(action.id(), existingIMBK.get().getCamundaBusinessKey(), action.oldValue());
		}

		final var imbk = existingIMBK.orElseGet(() -> extEl.addExtensionElement(CamundaIn.class));
		imbk.setCamundaBusinessKey(action.newValue());
	}
}
