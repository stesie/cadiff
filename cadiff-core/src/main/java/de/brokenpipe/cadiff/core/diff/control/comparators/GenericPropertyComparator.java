package de.brokenpipe.cadiff.core.diff.control.comparators;

import de.brokenpipe.cadiff.core.actions.*;
import de.brokenpipe.cadiff.core.diff.entity.CompareContext;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class GenericPropertyComparator implements Comparator {

	private static final Collection<GenericPropertyMappingRecord<?, ?>> MAPPINGS = List.of(

			new GenericPropertyMappingRecord<BaseElement, String>(BaseElement.class,
					el -> Optional.ofNullable(el.getExtensionElements())
							.stream()
							.flatMap(x -> x.getElementsQuery().filterByType(CamundaIn.class).list().stream())
							.map(CamundaIn::getCamundaBusinessKey)
							.filter(Objects::nonNull)
							.findFirst()
							.orElse(null),
					String.class, ChangeCamundaBusinessKeyAction.class),

			new GenericPropertyMappingRecord<>(BusinessRuleTask.class, BusinessRuleTask::getCamundaDecisionRef,
					String.class, ChangeCamundaDecisionRefAction.class),
			new GenericPropertyMappingRecord<>(BusinessRuleTask.class, BusinessRuleTask::getCamundaMapDecisionResult,
					String.class, ChangeCamundaMapDecisionResultAction.class),
			new GenericPropertyMappingRecord<>(BusinessRuleTask.class, BusinessRuleTask::getCamundaResultVariable,
					String.class, ChangeCamundaResultVariableAction.class),

			new GenericPropertyMappingRecord<>(CallActivity.class, CallActivity::getCalledElement,
					String.class, ChangeCalledElementAction.class),
			new GenericPropertyMappingRecord<>(CallActivity.class,
					CallActivity::getCamundaVariableMappingDelegateExpression,
					String.class, ChangeCamundaVariableMappingDelegateExpressionAction.class),

			new GenericPropertyMappingRecord<>(FlowElement.class, FlowElement::getName,
					String.class, ChangeNameAction.class),

			new GenericPropertyMappingRecord<>(FlowNode.class, FlowNode::isCamundaAsyncAfter,
					Boolean.class, ChangeCamundaAsyncAfterAction.class),
			new GenericPropertyMappingRecord<>(FlowNode.class, FlowNode::isCamundaAsyncBefore,
					Boolean.class, ChangeCamundaAsyncBeforeAction.class),
			new GenericPropertyMappingRecord<>(FlowNode.class, FlowNode::isCamundaExclusive,
					Boolean.class, ChangeCamundaExclusiveAction.class),

			new GenericPropertyMappingRecord<>(Participant.class, Participant::getName,
					String.class, ChangeParticipantNameAction.class),
			new GenericPropertyMappingRecord<>(Participant.class,
					p -> Optional.ofNullable(p.getProcess()).map(BaseElement::getId).orElse(null),
					String.class, ChangeParticipantProcessAction.class),

			new GenericPropertyMappingRecord<>(SendTask.class, SendTask::getCamundaDelegateExpression,
					String.class, ChangeCamundaDelegateExpressionAction.class),

			new GenericPropertyMappingRecord<>(ServiceTask.class, ServiceTask::getCamundaClass,
					String.class, ChangeCamundaClassAction.class),
			new GenericPropertyMappingRecord<>(ServiceTask.class, ServiceTask::getCamundaExpression,
					String.class, ChangeCamundaExpressionAction.class),
			new GenericPropertyMappingRecord<>(ServiceTask.class, ServiceTask::getCamundaDelegateExpression,
					String.class, ChangeCamundaDelegateExpressionAction.class),

			new GenericPropertyMappingRecord<>(UserTask.class, UserTask::getCamundaCandidateGroups,
					String.class, ChangeCamundaCandidateGroupsAction.class),
			new GenericPropertyMappingRecord<>(UserTask.class, UserTask::getCamundaDueDate,
					String.class, ChangeCamundaDueDateAction.class),
			new GenericPropertyMappingRecord<>(UserTask.class, UserTask::getCamundaFormKey,
					String.class, ChangeCamundaFormKeyAction.class)

	);

	@Override
	public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
		return MAPPINGS.stream().flatMap(mapping -> mapping.apply(compareContext));
	}

	record GenericPropertyMappingRecord<E extends BaseElement, T>(
			Class<E> elementClass, Function<E, T> accessor, Class<T> propertyClass,
			Class<? extends ChangePropertyAction<T>> clazz)
			implements Comparator {

		@Override
		public Stream<Action> apply(final CompareContext<? extends BaseElement> compareContext) {
			if (!elementClass.isInstance(compareContext.from()) || !elementClass.isInstance(compareContext.to())) {
				return Stream.empty();
			}

			//noinspection unchecked
			return PropertyComparator.compareProperty(accessor, propertyClass, clazz,
					(CompareContext<E>) compareContext);
		}
	}
}
