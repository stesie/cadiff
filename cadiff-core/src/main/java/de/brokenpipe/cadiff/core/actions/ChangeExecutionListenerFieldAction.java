package de.brokenpipe.cadiff.core.actions;

import de.brokenpipe.cadiff.core.ExecutionListenerKey;
import de.brokenpipe.cadiff.core.patch.control.patchers.ChangeExecutionListenerFieldPatcher;
import de.brokenpipe.cadiff.core.patch.control.patchers.Patcher;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaField;

public record ChangeExecutionListenerFieldAction(String id, ExecutionListenerKey key, String fieldName,
												 Config oldValue, Config newValue) implements SingleIdRelatedAction {
	@Override
	public Patcher patcher() {
		return new ChangeExecutionListenerFieldPatcher(this);
	}

	public record Config(String source, boolean isSourceExpression) {
		public static Config ofSource(final String source) {
			return new Config(source, false);
		}

		public static Config ofSourceExpression(final String sourceExpression) {
			return new Config(sourceExpression, true);
		}

		public static ChangeExecutionListenerFieldAction.Config from(final CamundaField field) {
			if (field.getCamundaExpressionChild() != null) {
				return Config.ofSourceExpression(field.getCamundaExpressionChild().getTextContent());
			} else {
				return Config.ofSource(field.getCamundaString().getTextContent());
			}
		}
	}
}
