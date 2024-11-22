package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeDocumentationAction;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DocumentationComparator implements StringPropertyComparator<BaseElement> {

	@Override
	public Stream<Action> apply(final BaseElement from, final BaseElement to) {
		return compareStringProperty(x -> Optional.ofNullable(x.getDocumentations())
				.filter(Predicate.not(Collection::isEmpty))
				.map(y -> y.iterator().next())
				.map(ModelElementInstance::getTextContent)
				.orElse(null), ChangeDocumentationAction.class, from, to);
	}
}
