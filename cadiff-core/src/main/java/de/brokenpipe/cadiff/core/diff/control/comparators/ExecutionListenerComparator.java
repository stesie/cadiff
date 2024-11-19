package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.Action;
import de.brokenpipe.cadiff.core.actions.AddExecutionListenerAction;
import de.brokenpipe.cadiff.core.actions.DeleteExecutionListenerAction;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Comparator for execution listeners. We don't try to identify updates. Just compare from and to lists and create
 * add and remove actions as needed.
 */
public class ExecutionListenerComparator implements Comparator {
	@Override
	public Stream<Action> apply(final BaseElement from, final BaseElement to) {
		final List<CamundaExecutionListener> fromListeners = Optional.ofNullable(from.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaExecutionListener.class).list())
				.orElse(Collections.emptyList());
		final List<CamundaExecutionListener> toListeners = Optional.ofNullable(to.getExtensionElements())
				.map(x -> x.getElementsQuery().filterByType(CamundaExecutionListener.class).list())
				.orElse(Collections.emptyList());

		return Stream.concat(
				toListeners.stream()
						.filter(a -> fromListeners.stream().noneMatch(b -> extensionListenerEquals(a, b)))
						.map(a -> createExecutionListener(to.getId(), a)),
				fromListeners.stream()
						.filter(a -> toListeners.stream().noneMatch(b -> extensionListenerEquals(a, b)))
						.map(a -> deleteExecutionListener(from.getId(), a))
		);
	}

	private Action deleteExecutionListener(final String id, final CamundaExecutionListener executionListener) {
		return new DeleteExecutionListenerAction(
				id,
				executionListener.getCamundaEvent(),
				executionListener.getCamundaClass(),
				executionListener.getCamundaDelegateExpression(),
				executionListener.getCamundaExpression()
		);	}

	private Action createExecutionListener(final String id, final CamundaExecutionListener executionListener) {
		return new AddExecutionListenerAction(
				id,
				executionListener.getCamundaEvent(),
				executionListener.getCamundaClass(),
				executionListener.getCamundaDelegateExpression(),
				executionListener.getCamundaExpression()
		);
	}

	public boolean extensionListenerEquals(final CamundaExecutionListener a, final CamundaExecutionListener b) {
		// FIXME handle camunda script & fields, maybe create update when fields are changed !?
		return Objects.equals(a.getCamundaEvent(), b.getCamundaEvent())
				&& Objects.equals(a.getCamundaClass(), b.getCamundaClass())
				&& Objects.equals(a.getCamundaDelegateExpression(), b.getCamundaDelegateExpression())
				&& Objects.equals(a.getCamundaExpression(), b.getCamundaExpression());
	}
}
