package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.ChangeAttachedToAction;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;

import java.util.Optional;
import java.util.stream.Stream;

public class AttachedToComparator extends UpcastComparator<BoundaryEvent> {

	@Override
	protected Class<BoundaryEvent> getClassType() {
		return BoundaryEvent.class;
	}

	@Override
	protected Stream<Action> compare(final CompareContext<BoundaryEvent> compareContext) {
		return PropertyComparator.compareStringProperty(e -> Optional.ofNullable(e.getAttachedTo())
						.map(BaseElement::getId)
						.orElse(null),
				ChangeAttachedToAction.class, compareContext);
	}
}
