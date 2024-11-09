package de.brokenpipe.cadiff.core.diff.control.voters;

import de.brokenpipe.cadiff.core.diff.entity.Vote;
import de.brokenpipe.cadiff.core.diff.entity.VoteContext;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

public class CamundaDelegateExpressionVoter implements Voter {

	@Override
	public Vote apply(final String removeId, final String addId, final VoteContext<? extends BaseElement> context) {
		final var removed = context.fromMap().get(removeId);
		final var added = context.toMap().get(addId);

		if (removed instanceof final ServiceTask removedServiceTask && added instanceof final ServiceTask addedServiceTask) {
			if (removedServiceTask.getCamundaDelegateExpression() == null && addedServiceTask.getCamundaDelegateExpression() == null) {
				return Vote.NEUTRAL;
			}

			if (removedServiceTask.getCamundaDelegateExpression() == null || addedServiceTask.getCamundaDelegateExpression() == null) {
				return Vote.DOWN;
			}

			return (removedServiceTask.getCamundaDelegateExpression().equals(addedServiceTask.getCamundaDelegateExpression()))
				? Vote.UP
				: Vote.DOWN;
		}

		return Vote.NEUTRAL;
	}

}
