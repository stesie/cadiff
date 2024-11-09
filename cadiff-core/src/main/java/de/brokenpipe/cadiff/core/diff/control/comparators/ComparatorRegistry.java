package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ComparatorRegistry implements Comparator {

	private final List<Comparator> comparators;

	public static final ComparatorRegistry INSTANCE = ComparatorRegistry.init();

	private static ComparatorRegistry init() {
		return new ComparatorRegistry(
				ServiceLoader.load(Comparator.class).stream()
						.map(ServiceLoader.Provider::get)
						.toList());
	}

	@Override
	public Stream<Action> apply(final FlowElement from, final FlowElement to) {
		return comparators.stream()
				.flatMap(comparator -> comparator.apply(from, to));
	}
}
