package de.brokenpipe.cadiff.cli.commands;

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
import java.util.concurrent.Callable;

import static org.fusesource.jansi.Ansi.ansi;

// GIT_EXTERNAL_DIFF: path old-file old-hex old-mode new-file new-hex new-mode
@Command(name = "git-diff", description = "git diff.external tool, to diff BPMN files")
public class GitDiff implements Callable<Integer> {

	@Parameters(arity = "1")
	String path;

	@Parameters(arity = "1")
	File oldFile;

	@Parameters(arity = "1")
	String oldHex;

	@Parameters(arity = "1")
	String oldMode;

	@Parameters(arity = "1")
	File newFile;

	@Parameters(arity = "1")
	String newHex;

	@Parameters(arity = "1")
	String newMode;

	@Option(names = { "--print-id-changes" }, description = "Print changes of element ids")
	boolean printIdChanges;

	@Option(names = { "--print-all-edge-deletes" }, description = "Print edge deletes, even if they are related to a node delete")
	boolean printAllEdgeDeletes;

	@Override
	public Integer call() {
		System.out.print(ansi().bold());
		System.out.printf("cadiff --git a/%s b/%s", path, path);
		System.out.println(ansi().reset());

		if (newMode.equals(".")) {
			System.out.println("The file was deleted.");
			return Integer.valueOf(0);
		}

		if (oldMode.equals(".")) {
			System.out.println("New file was added.");
			return Integer.valueOf(0);
		}

		System.out.print(ansi().bold());
		System.out.printf("index %s..%s %s", oldHex.substring(0, 9), newHex.substring(0, 9), oldMode);
		System.out.println(ansi().reset());

		final var from = Bpmn.readModelFromFile(oldFile);
		final var to = Bpmn.readModelFromFile(newFile);

		final ChangeSet changeSet = new DiffCommand(from, to).execute();

		new ChangeSetPrinter(ActionPrintContext.of(changeSet, from, to, printIdChanges, printAllEdgeDeletes))
				.printAll();

		System.out.println();

		return Integer.valueOf(0);
	}

	public static void main(final String[] args) {
		final int exitCode = new CommandLine(new GitDiff()).execute(args);
		System.exit(exitCode);
	}

}