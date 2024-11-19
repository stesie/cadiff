package de.brokenpipe.cadiff.core.patch.control.patchers;

import de.brokenpipe.cadiff.core.actions.ChangeSequenceFlowAction;
import de.brokenpipe.cadiff.core.patch.control.patchers.exceptions.ValueMismatchException;
import de.brokenpipe.cadiff.core.patch.entity.PatcherContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

@RequiredArgsConstructor
public class ChangeSequenceFlowPatcher extends AbstractPatcher implements Patcher {

	private final ChangeSequenceFlowAction action;

	@Override
	public void accept(final PatcherContext context) {
		final SequenceFlow sequenceFlow = findTargetWithType(context, action.id(), SequenceFlow.class);

		if (!action.oldSourceId().equals(action.newSourceId())) {
			final String actualOldSourceId = sequenceFlow.getSource().getId();

			if (!action.oldSourceId().equals(actualOldSourceId)) {
				throw new ValueMismatchException(action.id(), actualOldSourceId, action.oldSourceId());
			}

			updateSequenceFlowSource(context, sequenceFlow, action.newSourceId());
		}

		if (!action.oldTargetId().equals(action.newTargetId())) {
			final String actualOldTargetId = sequenceFlow.getTarget().getId();

			if (!action.oldTargetId().equals(actualOldTargetId)) {
				throw new ValueMismatchException(action.id(), actualOldTargetId, action.oldTargetId());
			}

			updateSequenceFlowTarget(context, sequenceFlow, action.newTargetId());
		}
	}
}
