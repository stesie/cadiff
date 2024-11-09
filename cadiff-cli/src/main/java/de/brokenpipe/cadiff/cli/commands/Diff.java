package de.brokenpipe.cadiff.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import de.brokenpipe.cadiff.cli.control.ChangeSetPrinter;
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

	@Option(names = { "-d", "--dump-changeset" }, paramLabel = "CHANGESET", description = "Dump changeset (in yaml format) to file")
	File dumpChangeSet;

	@Override
	public Integer call() {
		final var from = Bpmn.readModelFromFile(fromFile);
		final var to = Bpmn.readModelFromFile(toFile);

		final ChangeSet changeSet = new DiffCommand(from, to).execute();

		if (dumpChangeSet != null) {
			dumpChangeSet(changeSet);
		}

		new ChangeSetPrinter(ActionPrintContext.of(changeSet, to)).printAll();

		return Integer.valueOf(0);
	}

	private void dumpChangeSet(final ChangeSet changeSet) {
		try {
			final var mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
			mapper.writeValue(dumpChangeSet, changeSet);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(final String[] args) {
		final int exitCode = new CommandLine(new Diff()).execute(args);
		System.exit(exitCode);
	}

}