# Multi-pane workspace

[multi-pane workspace](def://?inline)

Think of Intellij user interface, that functionality is implemented by [MultiPaneWorkspace](class://).

Each workspace has nine main areas:

- `left icon bar`
- `left top tool area`
- `left middle tool area`
- `bottom left tool area`
- `center area`
- `bottom right tool area`
- `right middle tool area`
- `right top tool area`
- `right icon bar`

- The areas (except the icon bars) are separated by dividers that let the user resize the given area.
- The tool areas:
    - contain [tool panes](def://) that provide supporting functions and information,
    - may be hidden or shown by the user by
        - clicking on the hide action in the pane title
        - clicking on an icon in the icon bars
- The center area contains a [centerPane](fragment://) fragment which manages [content pane groups](def://):
    - there must be at least one [content pane group](def://) in the workspace
    - there may be more than one [content pane group](def://) in the workspace
    - a [content pane group](def://) may be
      - singular - only one [content pane](def://) is shown without tabs
      - non-singular - multiple [content panes](def://) can be selected with tabs

## Icon bars

The icon bars:

- have fix width, maximum height
- are typically always shown (can be hidden if you really want)
- have icon groups

Icon groups are:

- automatically calculated from [panes](property://MultiPaneWorkspace)
- optional (if there is no pane for that group, it won't show)
- paired with workspace areas (except more)

| Group          | Icon bar | Where it loads the pane  |
|----------------|----------|--------------------------|
| `direct`       | left     | `center area`            |
| `left top`     | left     | `left top tool area`     |
| `left middle`  | left     | `left middle tool area`  |
| `more`         | left     | -                        |
| `left bottom`  | left     | `bottom left tool area`  |
| `right top`    | right    | `right top tool area`    |
| `right middle` | right    | `right middle tool area` |
| `right bottom` | right    | `bottom right tool area` |

Each icon belongs to a pane in [panes](property://MultiPaneWorkspace).

When a user clicks on an icon, the reaction depends on:

- the icon group
- the current state of the workspace

- `direct` group
    - the pane is loaded to the `center-area`
- `more`
    - a menu is shown so the user can add more tools to the icon bars
- other groups
    - if the pane is currently shown, it is unloaded and the area becomes hidden
    - if the other pane is in the given area, the new pane replaces it
    - if the area is hidden, it becomes shown and the pane is loaded into it

## Tool panes

[tool panes](def://) typically have a [tool pane header](def://) that displays
the name of the tool, a hide icon and possibly some [tool pane actions](def://). 

The [toolPane](fragment://) fragment automatically adds these components.

## Content panes

- Content panes are loaded into the center area.
- The center area always contains a [centerPane](fragment://) fragment.
- [centerPane](fragment://) manages content pane groups

For both cases [activeContentPane](property://MultiPaneWorkspace) contains the pane which is currently active.

## centerPane

- The [centerPane](fragment://) fragment can be loaded to the center to manage multiple panes in pane groups.
- Each pane group must have at least one content pane and may have more than one content pane.
- The pane groups are separated by sliders which can be used to resize the groups.
- Each pane group:
  - has a [ContentPaneGroupViewBackend](class://) in [contentPaneGroups](property://MultiPaneWorkspace)
  - has a [contentPaneGroup](fragment://) fragment

The [contentPaneGroup](fragment://):
  - can be singular (one pane only, no tabs) or non-singular (multiple panes, tabs)
  - uses a [tabContainer](fragment://) when non-singular
  - has an active pane
  - the UUID of the active pane is stored in [activePane](property://ContentPaneGroupViewBackend)

To add a content pane, use the [addContent](function://MultiPaneWorkspace) function.

[addContent](function://MultiPaneWorkspace):
- offers the `item` to the existing panes in this order:
  - last active pane
  - active pane in each pane group
  - other panes in each pane group
- creates a new pane by calling `paneFun` and depending on the `singularity` of the pane
  - `SINGULAR`: removes **all** active pane groups and adds a new, singular one with the pane
  - `NON_SINGULAR`:
    - if there is at least one non-singular pane group
      - adds the pane to the last active non-singular pane group
    - if there is no non-singular pane group
      - replaces the last active singular pane group with a new non-singular one
      - adds the pane to the new pane group

## Workspace state

[MultiPaneWorkspace](class://) stores the state of the workspace. When you use the application
framework from [lib-app](def://), the workspace is automatically created during application startup.
See [Application](guide://) for more information.

To create a [multi-pane workspace](def://) manually:

[createMultiPaneWorkspace](example://multiPaneWorkspaceExamples)

The [state variable](def://) `workspace` typically never changes, instead it contains [observable](def://) properties
fragments can observe to react to changes.

The [localContext](fragment://) fragment makes it possible for all inner fragments to find the workspace instance, 
as the example below shows. See [Local context](guide://) for details about [local contexts](def://).

[findWorkspace](example://multiPaneWorkspaceExamples)

## See also

- [Writing a tool pane](guide://)
- [Writing a content pane](guide://)