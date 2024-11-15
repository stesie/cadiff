package de.brokenpipe.cadiff.cli.commands;

import de.brokenpipe.cadiff.cli.control.ChangeSetPrinter;
import de.brokenpipe.cadiff.cli.control.Jackson;
import de.brokenpipe.cadiff.cli.control.SelftestControl;
import de.brokenpipe.cadiff.cli.entity.ActionPrintContext;
import de.brokenpipe.cadiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.cadiff.core.diff.entity.ChangeSet;
import org.camunda.bpm.model.bpmn.Bpmn;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Command(name = "diff", description = "Diff two BPMN files")
public class Diff implements Callable<Integer> {

	@Parameters(arity = "1", description = "BPMN file to compare against")
	File fromFile;

	@Parameters(arity = "1", description = "BPMN file to compare")
	File toFile;

	@Option(names = { "-d",
			"--dump-changeset" }, paramLabel = "CHANGESET", description = "Dump changeset (in yaml format) to file")
	File dumpChangeSet;

	@Option(names = { "-q", "--quiet" }, description = "Do not print human-friendly comparison")
	boolean quiet;

	@Option(names = { "--print-id-changes" }, description = "Print changes of element ids")
	boolean printIdChanges;

	@Option(names = { "--print-all-edge-deletes" }, description = "Print edge deletes, even if they are related to a node delete")
	boolean printAllEdgeDeletes;

	@Option(names = { "--selftest" }, description = "Perform selftest (test if the changeset is complete)")
	boolean performSelftest;

	@Override
	public Integer call() {
		final var from = Bpmn.readModelFromFile(fromFile);
		final var to = Bpmn.readModelFromFile(toFile);

		final ChangeSet changeSet = new DiffCommand(from, to).execute();

		if (dumpChangeSet != null) {
			dumpChangeSet(changeSet);
		}

		if (!quiet) {
			new ChangeSetPrinter(ActionPrintContext.of(changeSet, from, to, printIdChanges, printAllEdgeDeletes))
					.printAll();
		}

		if (performSelftest) {
			new SelftestControl(from, to, changeSet).execute();
		}

		return Integer.valueOf(0);
	}

	private void dumpChangeSet(final ChangeSet changeSet) {
		try {
			Jackson.MAPPER.writeValue(dumpChangeSet, changeSet);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(final String[] args) {
		final int exitCode = new CommandLine(new Diff()).execute(args);
		System.exit(exitCode);
	}

}