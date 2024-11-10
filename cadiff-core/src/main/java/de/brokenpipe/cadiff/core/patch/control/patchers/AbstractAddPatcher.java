package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.Waypoint;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.Incoming;
import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;

import java.util.List;
import java.util.Optional;

public abstract class AbstractAddPatcher {

	protected void addFlowElement(final BpmnModelInstance bpmnModelInstance, final String id, final String typeName,
			final de.brokenpipe.cadiff.core.Bounds actionBounds) {
		final var elementType = bpmnModelInstance.getModel().getTypes().stream()
				.filter(x -> x.getTypeName().equals(typeName))
				.findFirst()
				.orElseThrow(); // FIXME

		final BaseElement addedElement = bpmnModelInstance.newInstance(elementType, id);

		final Process process = findProcess(bpmnModelInstance);
		process.addChildElement(addedElement);

		final var di = bpmnModelInstance.newInstance(BpmnShape.class, id + "_di");
		di.setBpmnElement(addedElement);

		final var bounds = bpmnModelInstance.newInstance(Bounds.class);
		di.setBounds(bounds);
		bounds.setX(actionBounds.x().doubleValue());
		bounds.setY(actionBounds.y().doubleValue());
		bounds.setWidth(actionBounds.width().doubleValue());
		bounds.setHeight(actionBounds.height().doubleValue());

		final var diagramRoot = findRootElementByType(bpmnModelInstance, Collaboration.class)
				.map(BaseElement::getDiagramElement)
				.orElse(process.getDiagramElement());
		diagramRoot.addChildElement(di);
	}

	protected void addSequenceFlow(final BpmnModelInstance bpmnModelInstance, final String id,
			final String sourceId, final String targetId, final List<Waypoint> waypoints) {

		final SequenceFlow addedElement = bpmnModelInstance.newInstance(SequenceFlow.class, id);

		final FlowNode source = bpmnModelInstance.getModelElementById(sourceId);
		addedElement.setSource(source);

		final Outgoing sourceOutgoing = bpmnModelInstance.newInstance(Outgoing.class);
		sourceOutgoing.setTextContent(addedElement.getId());
		source.addChildElement(sourceOutgoing);

		final FlowNode target = bpmnModelInstance.getModelElementById(targetId);
		addedElement.setTarget(target);

		final Incoming targetIncoming = bpmnModelInstance.newInstance(Incoming.class);
		targetIncoming.setTextContent(addedElement.getId());
		target.addChildElement(targetIncoming);

		final Process process = findProcess(bpmnModelInstance);
		process.addChildElement(addedElement);

		final var di = bpmnModelInstance.newInstance(BpmnEdge.class, id + "_di");
		di.setBpmnElement(addedElement);

		waypoints.stream()
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

	protected static <T extends BaseElement> Optional<T> findRootElementByType(final BpmnModelInstance bpmnModelInstance, final Class<T> type) {
		return bpmnModelInstance.getDefinitions().getRootElements().stream()
				.filter(x -> type.isAssignableFrom(x.getClass()))
				.map(type::cast)
				.findFirst();
	}

	protected static Process findProcess(final BpmnModelInstance bpmnModelInstance) {
		return findRootElementByType(bpmnModelInstance, Process.class)
				.orElseThrow(IllegalStateException::new);
	}

}
