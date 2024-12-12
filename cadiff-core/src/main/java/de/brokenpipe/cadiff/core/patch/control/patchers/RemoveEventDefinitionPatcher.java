package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import org.camunda.bpm.model.bpmn.instance.EventDefinition;

public class RemoveEventDefinitionPatcher extends AbstractChangeEventDefinitionPatcher<EventDefinition> {

	public RemoveEventDefinitionPatcher(final String id, final String definitionId) {
		super(id, definitionId, EventDefinition.class);
	}

	@Override
	protected void applyChange(final PatcherContext context, final EventDefinition def) {
		def.getParentElement().removeChildElement(def);
	}
}
