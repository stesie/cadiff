package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.Waypoint;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.TargetElementNotFoundException;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.UnexpectedTargetElementTypeException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.Incoming;
import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Collaboration;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.List;
import java.util.Optional;

public abstract class AbstractPatcher {

	protected void addFlowElement(final PatcherContext context, final String id, final String typeName,
			final de.brokenpipe.cadiff.core.Bounds actionBounds) {
		final var elementType = context.getModelInstance().getModel().getTypes().stream()
				.filter(x -> x.getTypeName().equals(typeName))
				.findFirst()
				.orElseThrow(); // FIXME

		final BaseElement addedElement = context.getModelInstance().newInstance(elementType, id);

		final BaseElement containerElement = context.getContainerElement();
		containerElement.addChildElement(addedElement);

		final var di = context.getModelInstance().newInstance(BpmnShape.class, id + "_di");
		di.setBpmnElement(addedElement);

		final var bounds = context.getModelInstance().newInstance(Bounds.class);
		di.setBounds(bounds);
		bounds.setX(actionBounds.x().doubleValue());
		bounds.setY(actionBounds.y().doubleValue());
		bounds.setWidth(actionBounds.width().doubleValue());
		bounds.setHeight(actionBounds.height().doubleValue());

		final var diagramRoot = findRootElementByType(context.getModelInstance(), Collaboration.class)
				.map(BaseElement::getDiagramElement)
				.orElse(containerElement.getDiagramElement());
		diagramRoot.addChildElement(di);
	}

	protected void addSequenceFlow(final PatcherContext context, final String id,
			final String sourceId, final String targetId, final List<Waypoint> waypoints) {

		final SequenceFlow addedElement = context.getModelInstance().newInstance(SequenceFlow.class, id);

		updateSequenceFlow(context, addedElement, sourceId, targetId);

		final BaseElement containerElement = context.getContainerElement();
		containerElement.addChildElement(addedElement);

		final var di = context.getModelInstance().newInstance(BpmnEdge.class, id + "_di");
		di.setBpmnElement(addedElement);

		waypoints.stream()
				.map(waypoint -> {
					final var wp = context.getModelInstance().newInstance(org.camunda.bpm.model.bpmn.instance.di.Waypoint.class);
					wp.setX(waypoint.x().doubleValue());
					wp.setY(waypoint.y().doubleValue());
					return wp;
				})
				.forEach(di::addChildElement);

		final var diagramRoot = findRootElementByType(context.getModelInstance(), Collaboration.class)
				.map(BaseElement::getDiagramElement)
				.orElse(containerElement.getDiagramElement());
		diagramRoot.addChildElement(di);

	}

	protected void updateSequenceFlow(final PatcherContext context, final SequenceFlow sequenceFlow,
			final String sourceId, final String targetId) {

		updateSequenceFlowSource(context, sequenceFlow, sourceId);

		updateSequenceFlowTarget(context, sequenceFlow, targetId);
	}

	protected void updateSequenceFlowSource(final PatcherContext context, final SequenceFlow sequenceFlow,
			final String sourceId) {

		if (sequenceFlow.getSource() != null) {
			sequenceFlow.getSource().getOutgoing().stream()
					.filter(x -> x.getId().equals(sequenceFlow.getId()))
					.findFirst()
					.ifPresent(c -> sequenceFlow.getSource().getDomElement().removeChild(c.getDomElement()));

		}

		final FlowNode source = context.getModelInstance().getModelElementById(sourceId);
		sequenceFlow.setSource(source);

		final Outgoing sourceOutgoing = context.getModelInstance().newInstance(Outgoing.class);
		sourceOutgoing.setTextContent(sequenceFlow.getId());
		source.addChildElement(sourceOutgoing);
	}

	protected void updateSequenceFlowTarget(final PatcherContext context, final SequenceFlow sequenceFlow,
			final String targetId) {

		if (sequenceFlow.getTarget() != null) {
			sequenceFlow.getTarget().getIncoming().stream()
					.filter(x -> x.getId().equals(sequenceFlow.getId()))
					.findFirst()
					.ifPresent(c -> sequenceFlow.getTarget().getDomElement().removeChild(c.getDomElement()));
		}

		final FlowNode target = context.getModelInstance().getModelElementById(targetId);
		sequenceFlow.setTarget(target);

		final Incoming targetIncoming = context.getModelInstance().newInstance(Incoming.class);
		targetIncoming.setTextContent(sequenceFlow.getId());
		target.addChildElement(targetIncoming);
	}

	protected void deleteElement(final BpmnModelInstance bpmnModelInstance, final String id) {
		final ModelElementInstance target = bpmnModelInstance.getModelElementById(id);

		if (target == null) {
			throw new TargetElementNotFoundException(id);
		}

		if (target instanceof final SequenceFlow sequenceFlow) {
			sequenceFlow.getDiagramElement().getParentElement().removeChildElement(sequenceFlow.getDiagramElement());
		}

		target.getParentElement().removeChildElement(target);
	}

	protected <T extends BaseElement> T findTargetWithType(final PatcherContext context, final String elementId,
			final Class<T> clazz) {
		final ModelElementInstance target = context.getModelInstance().getModelElementById(elementId);

		if (target == null) {
			throw new TargetElementNotFoundException(elementId);
		}

		if (clazz.isInstance(target)) {
			return clazz.cast(target);
		}

		throw new UnexpectedTargetElementTypeException(elementId, target.getClass().getSimpleName(), clazz.getSimpleName());
	}

	protected static <T extends BaseElement> Optional<T> findRootElementByType(final BpmnModelInstance bpmnModelInstance, final Class<T> type) {
		return bpmnModelInstance.getDefinitions().getRootElements().stream()
				.filter(x -> type.isAssignableFrom(x.getClass()))
				.map(type::cast)
				.findFirst();
	}

}
