package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeCamundaDelegateExpressionAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import org.camunda.bpm.model.bpmn.instance.SendTask;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.Task;

public class ChangeCamundaDelegateExpressionPatcher extends AbstractChangePropertyPatcher<Task, String> {

	public ChangeCamundaDelegateExpressionPatcher(final ChangeCamundaDelegateExpressionAction action) {
		super(action, Task.class,
				task -> switch (task) {
					case final ServiceTask serviceTask -> serviceTask.getCamundaDelegateExpression();
					case final SendTask sendTask -> sendTask.getCamundaDelegateExpression();
					default -> throw new UnexpectedTargetElementTypeException(task.getId(),
							task.getClass().getSimpleName(), null);
				},
				(task, value) -> {
					switch (task) {
					case final ServiceTask serviceTask -> serviceTask.setCamundaDelegateExpression(value);
					case final SendTask sendTask -> sendTask.setCamundaDelegateExpression(value);
					default -> throw new UnexpectedTargetElementTypeException(task.getId(),
							task.getClass().getSimpleName(), null);
					}
				});
	}
}
