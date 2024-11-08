package de.brokenpipe.bpmndiff.core.diff.control;

import de.brokenpipe.bpmndiff.core.actions.Action;
import de.brokenpipe.bpmndiff.core.diff.control.comparators.ComparatorRegistry;
import de.brokenpipe.bpmndiff.core.exceptions.NotImplementedException;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.Collection;
import java.util.stream.Stream;

public class FlowElementWalker extends AbstractWalker<FlowElement> {

	public FlowElementWalker(final Collection<FlowElement> from, final Collection<FlowElement> to) {
		super(from, to);
	}

	@Override
	protected Stream<Action> handleUpdated(final FlowElement from, final FlowElement to) {
		return ComparatorRegistry.INSTANCE.apply(from, to);
	}

	@Override
	protected Stream<Action> handleAdded(final FlowElement added) {
		throw new NotImplementedException();
	}
}
