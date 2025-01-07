package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeOutMappingAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaOut;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeOutMappingPatcher extends AbstractPatcher implements Patcher {

	private final ChangeOutMappingAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());
		final List<CamundaOut> outs = extEl.getElementsQuery().filterByType(CamundaOut.class).list();

		if (action.oldValue() == null) {
			if (outs.stream().map(ChangeOutMappingAction.Config::from).anyMatch(action.newValue()::equals)) {
				// same mapping already exists -> silently ignore
				return;
			}

			final var out = extEl.addExtensionElement(CamundaOut.class);
			updateFromAction(out);
			return;
		}

		final Optional<CamundaOut> existingOut = outs.stream()
				.filter(x -> x.getCamundaTarget().equals(action.targetName()))
				.findFirst();

		if (existingOut.isEmpty()) {
			// FIXME provide custom exception and provide targetName
			throw new TargetElementNotFoundException(action.id());
		}

		if (action.newValue() == null) {
			extEl.removeChildElement(existingOut.get());
			return;
		}

		updateFromAction(existingOut.get());
	}

	private void updateFromAction(final CamundaOut out) {
		out.setCamundaTarget(action.targetName());
		out.setCamundaLocal(action.newValue().local());
		if (action.newValue().isSourceExpression()) {
			out.setCamundaSourceExpression(action.newValue().source());
		} else {
			out.setCamundaSource(action.newValue().source());
		}
	}
}
