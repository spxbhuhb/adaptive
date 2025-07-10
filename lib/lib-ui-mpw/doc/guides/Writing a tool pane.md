# Writing a tool pane

[tool pane](def://?inline)

## Implementation

To implement a [tool pane](def://):

1. Implement the [view backend](def://) for the pane.
2. Implement the [UI fragment](def://) for the pane.
3. Add the [tool pane](def://) to the [application module definition](def://).
   1. Define a [fragment key](def://) 
   2. Create a [pane definition](def://)
   3. Register the [pane definition](def://) in the [multi-pane workspace](def://)

### View backend implementation

[Tool pane](def://) [view backends](def://) extend [PaneViewBackend](class://). 

They have to implement the [paneDef](property://PaneViewBackend) and [workspace](proprerty://PaneViewBackend)
properties, but apart from that there are no other requirements.

If the tool uses the built-in [toolPane](fragment://) fragment and has [tool pane actions](def://), it should
override [paneActions](function://PaneViewBackend) as well.

[ExampleToolViewBackend](example://)

### UI fragment implementation

[Tool pane](def://) [UI fragments](def://) are standard [UI fragments](def://) with a [local context](def://)
that contains their [view backend](def://).

Use the [viewBackend](function://) convenience function to get the backend easily. Note that
[viewBackend](function://) is a [producer](def://) and whenever the backend changes it will
notify the fragment which starts the standard [patching](def://) and updates the UI.

[exampleToolPane](example://)

### Adding the tool pane

The [tool pane](def://) is usually added to the [application module definition](def://). For more
information about [application modules](def://) see [Application](guide://).

Adding the tool pane involves these steps:

1. Use the [fragmentKey](function://AppModule) function to define a [fragment key](def://).
2. Create a pane definition with [PaneDef](class://)
3. register the [UI fragment](def://) in [frontendAdapterInit](function://AppModule)
4. register the pane backend in [workspaceInit](function://AppModule)

[ExampleToolPaneModule](example://)