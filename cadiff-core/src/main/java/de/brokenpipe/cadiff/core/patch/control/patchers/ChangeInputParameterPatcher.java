package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.actions.ChangeInputParameterAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.NameAlreadyPresentException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.NameNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.NameValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeInputParameterPatcher extends AbstractPatcher implements Patcher {

	private final ChangeInputParameterAction action;

	@Override
	public void accept(final PatcherContext context) {
		final ExtensionElements extEl = findExtensionElements(context, action.id());

		final CamundaInputOutput inputOutput = extEl.getElementsQuery()
				.filterByType(CamundaInputOutput.class).list().stream()
				.findFirst()
				.orElseGet(() -> {
					final var newInputOutput = context.getModelInstance().newInstance(CamundaInputOutput.class);
					extEl.addChildElement(newInputOutput);
					return newInputOutput;
				});

		final Optional<CamundaInputParameter> existing = inputOutput.getCamundaInputParameters().stream()
				.filter(x -> x.getCamundaName().equals(action.name()))
				.findFirst();

		if (action.oldValue() == null) {
			// handle add
			if (existing.isPresent()) {
				throw new NameAlreadyPresentException(action.id(), action.name());
			}

			final var newInput = inputOutput.getModelInstance().newInstance(CamundaInputParameter.class);
			newInput.setCamundaName(action.name());
			action.newValue().accept(newInput);

			inputOutput.getCamundaInputParameters().add(newInput);
			return;
		}

		if (existing.isEmpty()) {
			throw new NameNotFoundException(action.id(), action.name());
		}

		if (action.newValue() == null) {
			// remove
			inputOutput.getCamundaInputParameters().remove(existing.get());
			return;
		}

		if (!Value.of(existing.get()).equals(action.oldValue())) {
			throw new NameValueMismatchException(action.id(), action.name(), action.oldValue(), action.newValue());
		}

		action.newValue().accept(existing.get());
	}
}
