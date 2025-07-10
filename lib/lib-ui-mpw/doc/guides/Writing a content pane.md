# Writing a content pane

[content pane](def://?inline)

## Implementation

To implement a [content pane](def://):

1. Implement the [view backend](def://) for the pane.
2. Implement the [UI fragment](def://) for the pane.
3. Add the [content pane](def://) to the [application module definition](def://).
   1. Define a [fragment key](def://) 
   2. Create a [pane definition](def://)
   3. Register the [content pane builder](def://) in the [multi-pane workspace](def://)

### View backend implementation

[Content pane](def://) [view backends](def://) extend [PaneViewBackend](class://). 

They have to implement the [paneDef](property://PaneViewBackend) and [workspace](proprerty://PaneViewBackend)
properties, but apart from that there are no other requirements.

[ExampleContentViewBackend](example://)

### UI fragment implementation

[Content pane](def://) [UI fragments](def://) are standard [UI fragments](def://) with a [local context](def://)
that contains their [view backend](def://).

Use the [viewBackend](function://) convenience function to get the backend easily. Note that
[viewBackend](function://) is a [producer](def://) and whenever the backend changes it will
notify the fragment which starts the standard [patching](def://) and updates the UI.

[exampleContentPane](example://)

### Adding the content pane

The [content pane](def://) is usually added to the [application module definition](def://). For more
information about [application modules](def://) see [Application](guide://).

Adding the content pane involves these steps:

1. Use the [fragmentKey](function://AppModule) function to define a [fragment key](def://).
2. Create a pane definition with [PaneDef](class://)
3. register the [UI fragment](def://) in [frontendAdapterInit](function://AppModule)
4. register the [content pane builder](def://) in [workspaceInit](function://AppModule) with [addContentPaneBuilder](function://AppModule)

[addContentPaneBuilder](function://AppModule) requires a [content type](def://), a condition and a builder function.

The [content type](def://) is used to filter the [content pane builders](def://), only builders with the
proper [content type](def://) are considered when adding a pane.

The condition has two purposes:

- it checks if the pane can handle the given content (returns with null if not), and
- it casts the pane content to the appropriate type to ensure type safety

The builder function creates a view backend that controls to the pane.

[ExampleContentPaneModule](example://)

If you display [values](def://) in your content pane, you can use the value check functions for
the condition with checks that the item is a [value](def://) with the right [spec](def://).

[ExampleTreeContentPaneModule](example://)