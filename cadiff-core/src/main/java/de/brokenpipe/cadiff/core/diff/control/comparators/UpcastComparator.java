package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.stream.Stream;

public abstract class UpcastComparator<T extends BaseElement> implements Comparator {

	protected abstract Class<T> getClassType();

	protected abstract Stream<Action> compare(CompareContext<T> compareContext);

	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		final Class<T> classType = getClassType();

		if (!classType.isInstance(compareContext.from()) || !classType.isInstance(compareContext.to())) {
			return Stream.empty();
		}

		return compare(compareContext.map(classType::cast));
	}

}
