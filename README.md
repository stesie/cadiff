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