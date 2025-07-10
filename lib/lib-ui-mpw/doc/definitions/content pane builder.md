# Pane builder function

Content pane builders create [content panes](def://). They are typically called when the user performs
an action intended to load some content into the center area of a [multi-pane workspace](def://).

For example, if a workspace has a documentation browser [tool pane](def://) and the user clicks
on a section, the content of the section is shown. In this case the [tool pane](def://) tells
the workspace to create a new [content pane](def://) which is done by calling the builder function
of pane builder.

Content pane builders are typically registered during application startup in the 
[application module definition](def://).

## See also

- [Multi-pane workspace](guide://)
- [Writing a content pane](guide://)