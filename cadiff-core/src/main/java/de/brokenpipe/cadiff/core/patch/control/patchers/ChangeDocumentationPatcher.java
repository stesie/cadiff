package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeDocumentationAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Documentation;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ChangeDocumentationPatcher extends AbstractPatcher implements Patcher {

	private final ChangeDocumentationAction action;

	@Override
	public void accept(final PatcherContext context) {
		final BaseElement targetElement = findTargetWithType(context, action.id(), BaseElement.class);

		final var oldValue =Optional.ofNullable(targetElement.getDocumentations())
				.filter(Predicate.not(Collection::isEmpty))
				.map(y -> y.iterator().next())
				.map(ModelElementInstance::getTextContent)
				.orElse(null);

		if (action.oldValue() == null
				? oldValue != null
				: !action.oldValue().equals(oldValue)) {
			throw new ValueMismatchException(action.id(), oldValue, action.oldValue());
		}

		if (action.newValue() == null) {
			targetElement.getDocumentations().clear();
			return;
		}

		final Documentation doc = context.getModelInstance().newInstance(Documentation.class);
		doc.setId(null);
		doc.setTextContent(action.newValue());

		targetElement.getDocumentations().add(doc);
	}
}
