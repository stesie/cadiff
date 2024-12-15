package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeInMappingAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChangeInMappingPatcher extends AbstractPatcher implements Patcher {

	private final ChangeInMappingAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());
		final List<CamundaIn> ins = extEl.getElementsQuery().filterByType(CamundaIn.class).list();

		if (action.oldValue() == null) {
			if (ins.stream().map(ChangeInMappingAction.Config::from).anyMatch(action.newValue()::equals)) {
				// same mapping already exists -> silently ignore
				return;
			}

			final var in = extEl.addExtensionElement(CamundaIn.class);
			updateFromAction(in);
			return;
		}

		final Optional<CamundaIn> existingIn = ins.stream()
				.filter(x -> x.getCamundaTarget().equals(action.targetName()))
				.findFirst();

		if (existingIn.isEmpty()) {
			// FIXME provide custom exception and provide targetName
			throw new TargetElementNotFoundException(action.id());
		}

		if (action.newValue() == null) {
			extEl.removeChildElement(existingIn.get());
			return;
		}

		updateFromAction(existingIn.get());
	}

	private void updateFromAction(final CamundaIn in) {
		in.setCamundaTarget(action.targetName());
		in.setCamundaLocal(action.newValue().local());
		if (action.newValue().isSourceExpression()) {
			in.setCamundaSourceExpression(action.newValue().source());
		} else {
			in.setCamundaSource(action.newValue().source());
		}
	}
}
