package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeDocumentationAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.brokenpipe.cadiff.core.diff.control.comparators.PropertyComparator.compareStringProperty;

public class DocumentationComparator implements Comparator {

	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		return compareStringProperty(x -> Optional.ofNullable(x.getDocumentations())
				.filter(Predicate.not(Collection::isEmpty))
				.map(y -> y.iterator().next())
				.map(ModelElementInstance::getTextContent)
				.orElse(null), ChangeDocumentationAction.class, compareContext);
	}
}
