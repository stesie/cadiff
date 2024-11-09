package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.Incoming;
import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;

import java.util.Optional;

@RequiredArgsConstructor
public class AddSimpleSequenceFlowPatcher implements Patcher {

	private final AddSimpleSequenceFlowAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {

		final SequenceFlow addedElement = bpmnModelInstance.newInstance(SequenceFlow.class, action.id());

		final FlowNode source = bpmnModelInstance.getModelElementById(action.sourceId());
		addedElement.setSource(source);

		final Outgoing sourceOutgoing = bpmnModelInstance.newInstance(Outgoing.class);
		sourceOutgoing.setTextContent(addedElement.getId());
		source.addChildElement(sourceOutgoing);

		final FlowNode target = bpmnModelInstance.getModelElementById(action.targetId());
		addedElement.setTarget(target);

		final Incoming targetIncoming = bpmnModelInstance.newInstance(Incoming.class);
		targetIncoming.setTextContent(addedElement.getId());
		target.addChildElement(targetIncoming);

		final Process process = findProcess(bpmnModelInstance);
		process.addChildElement(addedElement);

		final var di = bpmnModelInstance.newInstance(BpmnEdge.class, action.id() + "_di");
		di.setBpmnElement(addedElement);

		action.waypoints().stream()
				.map(waypoint -> {
					final var wp = bpmnModelInstance.newInstance(org.camunda.bpm.model.bpmn.instance.di.Waypoint.class);
					wp.setX(waypoint.x().doubleValue());
					wp.setY(waypoint.y().doubleValue());
					return wp;
				})
				.forEach(di::addChildElement);

		final var diagramRoot = findRootElementByType(bpmnModelInstance, Collaboration.class)
				.map(BaseElement::getDiagramElement)
				.orElse(process.getDiagramElement());
		diagramRoot.addChildElement(di);

	}

	private static org.camunda.bpm.model.bpmn.instance.Process findProcess(final BpmnModelInstance bpmnModelInstance) {
		return bpmnModelInstance.getDefinitions().getRootElements().stream()
				.filter(x -> x instanceof org.camunda.bpm.model.bpmn.instance.Process)
				.map(Process.class::cast)
				.findFirst()
				.orElseThrow(IllegalStateException::new);
	}

	private static <T extends BaseElement> Optional<T> findRootElementByType(final BpmnModelInstance bpmnModelInstance, final Class<T> type) {
		return bpmnModelInstance.getDefinitions().getRootElements().stream()
				.filter(x -> type.isAssignableFrom(x.getClass()))
				.map(type::cast)
				.findFirst();
	}
}
