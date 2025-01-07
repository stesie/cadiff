package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeOutMappingPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaOut;

public record ChangeOutMappingAction(String id, String targetName, Config oldValue, Config newValue)
		implements SingleIdRelatedAction {

	@Override
	public Patcher patcher() {
		return new ChangeOutMappingPatcher(this);
	}

	public record Config(String source, boolean isSourceExpression, boolean local) {
		public static Config ofSource(final String source, final boolean local) {
			return new Config(source, false, local);
		}

		public static Config ofSourceExpression(final String sourceExpression, final boolean local) {
			return new Config(sourceExpression, true, local);
		}

		public static ChangeOutMappingAction.Config from(final CamundaOut element) {
			if (element.getCamundaSourceExpression() != null) {
				return Config.ofSourceExpression(
						element.getCamundaSourceExpression(),
						element.getCamundaLocal());
			} else {
				return Config.ofSource(
						element.getCamundaSource(),
						element.getCamundaLocal());
			}
		}
	}
}
