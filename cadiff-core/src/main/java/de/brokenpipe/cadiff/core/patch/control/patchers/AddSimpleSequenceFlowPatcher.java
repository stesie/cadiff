package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleSequenceFlowAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;

@RequiredArgsConstructor
public class AddSimpleSequenceFlowPatcher implements Patcher {

	private final AddSimpleSequenceFlowAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {

		final SequenceFlow addedElement = bpmnModelInstance.newInstance(SequenceFlow.class, action.id());
		addedElement.setSource(bpmnModelInstance.getModelElementById(action.sourceId()));
		addedElement.setTarget(bpmnModelInstance.getModelElementById(action.targetId()));

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

		process.getDiagramElement().addChildElement(di);

		//final var lookup = bpmnModelInstance.getModelElementById(action.id());

		//flowNode.getModelInstance().newInstance()
		// return Optional.of(new de.brokenpipe.cadiff.core.actions.AddFlowNodeAction((org.camunda.bpm.model.bpmn.instance.FlowNode) addedElement));

	}

	private static org.camunda.bpm.model.bpmn.instance.Process findProcess(final BpmnModelInstance bpmnModelInstance) {
		return bpmnModelInstance.getDefinitions().getRootElements().stream()
				.filter(x -> x instanceof org.camunda.bpm.model.bpmn.instance.Process)
				.map(Process.class::cast)
				.findFirst()
				.orElseThrow(IllegalStateException::new);
	}
}
