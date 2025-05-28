# Tool pane

Tool pane is a subtype of [pane](def://) that shows a tool in a [multi-pane workspace](def://). Tools
can be anything from navigation aids, inspection tools, feedback providers, etc.

In many cases tools let the user add a [content pane](def://) to the [multi-pane workspace](def://). For
example, a documentation browser tool may let the user open a section of the documentation.

Tool panes typically consist of three parts: 

- a [UI fragment](def://) that renders the pane
- a [view backend](def://) that contains the logic of the pane
- a [pane definition](def://) that defines the pane for the [multi-pane workspace](def://)

## See also

- [what is a multi-pane workspace](guide://)
- [content pane](def://)