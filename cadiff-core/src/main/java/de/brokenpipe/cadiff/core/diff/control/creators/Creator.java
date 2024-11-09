package de.brokenpipe.cadiff.core.diff.control.creators;

import de.brokenpipe.cadiff.core.actions.AddAction;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;

import java.util.Optional;
import java.util.function.BiFunction;

public interface Creator extends BiFunction<String, VoteContext<? extends BaseElement>, Optional<AddAction>> {

	Integer getPriority();
}
