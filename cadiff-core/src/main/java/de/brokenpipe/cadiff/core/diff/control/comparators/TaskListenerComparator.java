package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddTaskListenerAction;
import de.brokenpipe.cadiff.core.actions.DeleteTaskListenerAction;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaTaskListener;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Comparator for execution listeners. We don't try to identify updates. Just compare from and to lists and create
 * add and remove actions as needed.
 */
public class TaskListenerComparator implements Comparator {
	@Override
	public Stream<Action> apply(final BaseElement from, final BaseElement to) {
		final List<CamundaTaskListener> fromListeners = Optional.ofNullable(from.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaTaskListener.class).list())
				.orElse(Collections.emptyList());
		final List<CamundaTaskListener> toListeners = Optional.ofNullable(to.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaTaskListener.class).list())
				.orElse(Collections.emptyList());

		return Stream.concat(
				toListeners.stream()
						.filter(a -> fromListeners.stream().noneMatch(b -> extensionListenerEquals(a, b)))
						.map(a -> createTaskListener(to.getId(), a)),
				fromListeners.stream()
						.filter(a -> toListeners.stream().noneMatch(b -> extensionListenerEquals(a, b)))
						.map(a -> deleteTaskListener(from.getId(), a))
		);
	}

	private Action deleteTaskListener(final String id, final CamundaTaskListener executionListener) {
		return new DeleteTaskListenerAction(
				id,
				executionListener.getCamundaEvent(),
				executionListener.getCamundaClass(),
				executionListener.getCamundaDelegateExpression(),
				executionListener.getCamundaExpression()
		);	}

	private Action createTaskListener(final String id, final CamundaTaskListener executionListener) {
		return new AddTaskListenerAction(
				id,
				executionListener.getCamundaEvent(),
				executionListener.getCamundaClass(),
				executionListener.getCamundaDelegateExpression(),
				executionListener.getCamundaExpression()
		);
	}

	public boolean extensionListenerEquals(final CamundaTaskListener a, final CamundaTaskListener b) {
		// FIXME handle camunda script & fields, maybe create update when fields are changed !?
		return Objects.equals(a.getCamundaEvent(), b.getCamundaEvent())
				&& Objects.equals(a.getCamundaClass(), b.getCamundaClass())
				&& Objects.equals(a.getCamundaDelegateExpression(), b.getCamundaDelegateExpression())
				&& Objects.equals(a.getCamundaExpression(), b.getCamundaExpression());
	}
}
