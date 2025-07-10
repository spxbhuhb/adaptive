# Pane

Pane refers to an individual, independently managed [UI fragment](def://) that serves 
as a dedicated section of a [multi-pane workspace](def://). 

[tool pane](def://) and [content pane](def://) are two distinct types of panes.

Panes are described by a [pane definition](def://) and always have a [view backend](def://). The definitions
are typically registered into the workspace during application startup while the backends
are typically created on-demand when the user first opens the pane.

## See also

- [Multi-pane workspace](guide://)