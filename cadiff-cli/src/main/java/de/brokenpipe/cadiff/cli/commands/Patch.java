package de.brokenpipe.cadiff.cli.commands;

import de.brokenpipe.cadiff.cli.control.Jackson;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import de.brokenpipe.cadiff.core.patch.boundary.PatchCommand;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "patch", description = "Apply changeset to BPMN file")
public class Patch implements Callable<Integer> {

	@Parameters(arity = "1", description = "bpmn file to patch")
	File fileToPatch;

	@Parameters(arity = "1", description = "changeset to apply")
	File changeSetFile;

	@Parameters(arity = "1", description = "output file")
	File outputFile;

	@Override
	public Integer call() throws Exception {
		final BpmnModelInstance bpmn = Bpmn.readModelFromFile(fileToPatch);

		final ChangeSet changeSet = Jackson.MAPPER.readValue(changeSetFile, ChangeSet.class);
		final ChangeSet rejections = new PatchCommand(bpmn, changeSet).execute();

		Bpmn.writeModelToFile(outputFile, bpmn);
		return Integer.valueOf(0);
	}

	public static void main(final String[] args) {
		final int exitCode = new CommandLine(new Patch()).execute(args);
		System.exit(exitCode);
	}

}