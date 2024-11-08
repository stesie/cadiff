package de.brokenpipe.bpmndiff.core.diff.control.comparators;

import de.brokenpipe.bpmndiff.core.actions.Action;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ComparatorRegistry implements Comparator {

	private final List<Comparator> comparators;

	public static ComparatorRegistry INSTANCE = new ComparatorRegistry(List.of(
			new NameComparator(),
			new CamundaAsyncAfterComparator(),
			new CamundaAsyncBeforeComparator(),
			new CamundaClassComparator(),
			new CamundaDelegateExpressionComparator(),
			new CamundaExclusiveComparator(),
			new CamundaExpressionComparator()));

	@Override
	public Stream<Action> apply(final FlowElement from, final FlowElement to) {
		return comparators.stream()
				.flatMap(comparator -> comparator.apply(from, to));
	}
}
