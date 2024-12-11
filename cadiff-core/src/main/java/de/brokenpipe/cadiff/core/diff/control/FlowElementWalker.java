package de.brokenpipe.cadiff.core.diff.control;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.diff.control.comparators.ComparatorRegistry;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.FlowElement;

import java.util.Collection;
import java.util.stream.Stream;

public class FlowElementWalker extends AbstractVoteAddWalker<FlowElement> {

	public FlowElementWalker(final CompareContext<Collection<FlowElement>> compareContext) {
		super(compareContext);
	}

	@Override
	protected Stream<Action> handleUpdated(final CompareContext<FlowElement> context) {
		return ComparatorRegistry.INSTANCE.apply(context);
	}

}
