package de.brokenpipe.cadiff.cli.control.printers;

import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.actions.*;

public class AddElementActionPrinter extends AbstractActionPrinter {
	@Override
	public boolean supports(final Action action) {
		return action instanceof AddSimpleFlowNodeAction
				|| action instanceof AddErrorAction
				|| action instanceof AddSignalAction
				|| action instanceof AddCollaborationAction
				|| action instanceof AddParticipantAction;
	}

	@Override
	public void accept(final ActionPrintContext context, final Action action) {
		final String id = switch(action) {
			case final AddSimpleFlowNodeAction addSimpleFlowNodeAction -> addSimpleFlowNodeAction.id();
			case final AddErrorAction addErrorAction -> addErrorAction.id();
			case final AddSignalAction addSignalAction -> addSignalAction.id();
			case final AddCollaborationAction addCollaborationAction -> addCollaborationAction.id();
			case final AddParticipantAction addParticipantAction -> addParticipantAction.id();
			default -> throw new IllegalStateException("Unexpected value: " + action);
		};

		startBlock(context, id, ChangeType.ADD);

		// remove name change for this element, we've just printed the new name
		removeChangeNameById(context, id);

		printAttributeChangesForId(context, id, ChangeType.ADD);
	}

}
