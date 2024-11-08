package de.brokenpipe.bpmndiff.cli.commands;

import de.brokenpipe.bpmndiff.core.diff.boundary.DiffCommand;
import de.brokenpipe.bpmndiff.core.diff.entity.ChangeSet;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "diff", description = "Diff two BPMN files")
public class Diff implements Callable<Integer> {

	@Parameters(arity = "2", description = "two bpmn files to compare")
	File[] files;

	@Override
	public Integer call() throws Exception {
		final ChangeSet changeSet = new DiffCommand(files[0], files[1]).execute();
		return Integer.valueOf(0);
	}

	public static void main(final String[] args) {
		final int exitCode = new CommandLine(new Diff()).execute(args);
		System.exit(exitCode);
	}

}