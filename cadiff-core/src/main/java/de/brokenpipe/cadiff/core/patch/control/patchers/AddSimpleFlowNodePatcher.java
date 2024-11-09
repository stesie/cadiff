package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.AddSimpleFlowNodeAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.Patcher;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;

@RequiredArgsConstructor
public class AddSimpleFlowNodePatcher implements Patcher {

	private final AddSimpleFlowNodeAction action;

	@Override
	public void accept(final BpmnModelInstance bpmnModelInstance) {
		final var typeName = action.elementTypeName();
		final var elementType = bpmnModelInstance.getModel().getTypes().stream()
				.filter(x -> x.getTypeName().equals(typeName))
				.findFirst()
				.orElseThrow(); // FIXME

		final BaseElement addedElement = bpmnModelInstance.newInstance(elementType, action.id());

		final Process process = findProcess(bpmnModelInstance);
		process.addChildElement(addedElement);

		final var di = bpmnModelInstance.newInstance(BpmnShape.class, action.id() + "_di");
		di.setBpmnElement(addedElement);

		final var bounds = bpmnModelInstance.newInstance(Bounds.class);
		di.setBounds(bounds);
		bounds.setX(action.bounds().x().doubleValue());
		bounds.setY(action.bounds().y().doubleValue());
		bounds.setWidth(action.bounds().width().doubleValue());
		bounds.setHeight(action.bounds().height().doubleValue());

		process.getDiagramElement().addChildElement(di);

	}

	private static Process findProcess(final BpmnModelInstance bpmnModelInstance) {
		return bpmnModelInstance.getDefinitions().getRootElements().stream()
				.filter(x -> x instanceof Process)
				.map(Process.class::cast)
				.findFirst()
				.orElseThrow(IllegalStateException::new);
	}
}
