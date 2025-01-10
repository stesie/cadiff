package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.Optional;
import java.util.function.Function;

/**
 * Creators are the second step in the diff process. They are responsible for creating actions in a way that describes
 * the changes as good as possible. There are "fallback" creators (with a low priority) that simply create the flow
 * nodes and sequence flows. More "specific" creators may use more "elegant" ways to describe the changes.
 */
public interface Creator extends Function<VoteContext<String, ? extends BaseElement>, Optional<AddAction>> {

	/**
	 * Execution priority of the creator. The higher the value, the earlier it is run.
	 */
	Integer getPriority();
}
