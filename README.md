cadiff
======

Camunda BPMN Diff Utils

**This currently is a better proof of concept than a working tool.**  
Or to put it differently: Don't expect too much from it (for now).

The goal is to provide a tool to both diff (i.e. "compare") two BPMN files and patch (i.e. apply the differences to
another BPMN file).

Also, this is Open Source Software, so feel free to contribute.

## Usage

### Compare two BPMN files (human-friendly output)

```bash
$ java -cp cadiff-cli/target/cadiff-cli.jar de.brokenpipe.cadiff.cli.commands.Diff foo.bpmn bar.bpmn
```

Should produce something like this (even using ANSI colors to highlight changes):

```
Update :            process : NewProcessId
 ->                    name : null                                     -> process name                            

Update :        serviceTask : Name of activity changed (Activity_DoStuff)
 ->                    name : Do Stuff                                 -> Name of activity changed                
 ->             asyncBefore : false                                    -> true                                    
 ->      delegateExpression : ${taskDoStuff}                           -> ${taskDoMoreStuff}                      
```

### Compare two BPMN files (YAML output)

```bash
$ java -cp cadiff-cli/target/cadiff-cli.jar de.brokenpipe.cadiff.cli.commands.Diff -qd changes.yaml foo.bpmn bar.bpmn
```

Should produce something like this:

```yaml
changes:
- type: "de.brokenpipe.cadiff.core.actions.ChangeIdAction"
  oldId: "Process_1nhm1wk"
  newId: "NewProcessId"
- type: "de.brokenpipe.cadiff.core.actions.processes.ChangeProcessNameAction"
  id: "NewProcessId"
  oldValue: null
  newValue: "process name"
- type: "de.brokenpipe.cadiff.core.actions.ChangeNameAction"
  id: "Activity_DoStuff"
  oldValue: "Do Stuff"
  newValue: "Name of activity changed"
- type: "de.brokenpipe.cadiff.core.actions.ChangeCamundaAsyncBeforeAction"
  id: "Activity_DoStuff"
  oldValue: false
  newValue: true
- type: "de.brokenpipe.cadiff.core.actions.ChangeCamundaDelegateExpressionAction"
  id: "Activity_DoStuff"
  oldValue: "${taskDoStuff}"
  newValue: "${taskDoMoreStuff}"
```

### Patch a BPMN file

```bash
$ java -cp cadiff-cli/target/cadiff-cli.jar de.brokenpipe.cadiff.cli.commands.Patch foo.bpmn changes.yaml new-foo.bpmn
```

### Usage with Git

You can use `cadiff` with Git to compare two versions of a BPMN file.

First add the following to the `.gitattributes` file in the root of your repository:

```
*.bpmn diff=bpmn
```

Then add the following to the `.git/config` file in the root of your repository:

```
[diff "bpmn"]
	command = java -cp <path>/cadiff/cadiff-cli/target/cadiff-cli.jar de.brokenpipe.cadiff.cli.commands.GitDiff
```

Make sure to pass `--ext-diff` to git invocations, e.g.:

```bash
$ git show 1fd2abf1c --ext-diff
```

## Architecture & Extensibility

The tool is built around the concept of "Actions". An action is a change that can be applied to a BPMN file. The
`Diff` command will compare two BPMN files and produce a list of actions, performing the following steps (in order):

1. Run so-called "voters" to determine which elements are likely "equal" in both files, yet have their id changed.
2. Run so-called "creators" to determine which elements are new in the second file, and which actions are needed to
   re-create them.
3. Run so-called "comparators" to determine which attributes are different in both files.

The `Patch` command will apply the actions to a BPMN file, using so-called "patchers".

Last but not least the `Diff` CLI command has the concept of so-called "action printers" to print the actions in a
human-friendly way.

Any of the aforementioned components can be extended by implementing the respective interfaces and registering them
using Java's native `ServiceLoader` mechanism.
