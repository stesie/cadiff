package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.Value;
import de.brokenpipe.cadiff.core.actions.ChangeOutputParameterAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.NameAlreadyPresentException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.NameNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.NameValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaOutputParameter;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeOutputParameterPatcher extends AbstractPatcher implements Patcher {

	private final ChangeOutputParameterAction action;

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

		final Optional<CamundaOutputParameter> existing = inputOutput.getCamundaOutputParameters().stream()
				.filter(x -> x.getCamundaName().equals(action.name()))
				.findFirst();

		if (action.oldValue() == null) {
			// handle add
			if (existing.isPresent()) {
				throw new NameAlreadyPresentException(action.id(), action.name());
			}

			final var newOutput = inputOutput.getModelInstance().newInstance(CamundaOutputParameter.class);
			newOutput.setCamundaName(action.name());
			action.newValue().accept(newOutput);

			inputOutput.getCamundaOutputParameters().add(newOutput);
			return;
		}

		if (existing.isEmpty()) {
			throw new NameNotFoundException(action.id(), action.name());
		}

		if (action.newValue() == null) {
			// remove
			inputOutput.getCamundaOutputParameters().remove(existing.get());
			return;
		}

		if (!Value.of(existing.get()).equals(action.oldValue())) {
			throw new NameValueMismatchException(action.id(), action.name(), action.oldValue(), action.newValue());
		}

		action.newValue().accept(existing.get());
	}
}
