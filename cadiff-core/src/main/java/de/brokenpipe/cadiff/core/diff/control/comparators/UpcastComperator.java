package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.stream.Stream;

public abstract class UpcastComperator<T extends FlowElement> implements Comparator {

	protected abstract Class<T> getClassType();

	protected abstract Stream<Action> compare(T from, T to);

	@Override
	public Stream<Action> apply(final FlowElement from, final FlowElement to) {
		final Class<T> classType = getClassType();

		if (!classType.isInstance(from) || !classType.isInstance(to)) {
			return Stream.empty();
		}

		return compare(classType.cast(from), classType.cast(to));
	}

}
