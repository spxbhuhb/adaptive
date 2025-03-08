# Workspace

`Workspace` is a very complex UI fragment capable of managing multiple tools and content editors together.

Each workspace has 9 main areas:

- `left icon bar`
- `left top tool area`
- `left middle tool area`
- `bottom left tool area`
- `center area`
- `bottom right tool area`
- `right middle tool area`
- `right top tool area`
- `right icon bar`

- The areas are separated by dividers which lets the user resize the given area.
- The tool areas:
    - contain tool panes that provide supporting functions and information,
    - may be hidden or shown by the user by
        - clicking on the hide action in the pane title
        - clicking on an icon in the icon bars
- The center area may contain:
    - a specific content pane, in this case you have to set up the loading/switch
    - a `wsCenterPane` which
      - manages content pane groups (at lease one, maybe more)

## Icon bars

The icon bars:

- have fix width, maximum height
- are typically always shown (can be hidden if you really want)
- have icon groups

Icon groups are:

- automatically calculated from `Workspace.panes`
- optional (if there is no pane for that group it won't show)
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

Each icon belongs to a pane in `Workspace.panes`.

When a user clicks on an icon, the reaction depends on:

- icon group
- current state of the workspace

- `direct` group
    - the pane is loaded to the `center-area`
- `more`
    - a menu is shown so the user can add more tools to the icon bars
- other groups
    - if the pane is currently shown, it is unloaded and the area becomes hidden
    - if the other pane is in the given area, the new pane replaces it
    - if the area is hidden, it becomes shown and the pane is loaded into it

## Pane title

Each pane may have a title. The `wsToolPane` fragment automatically adds a title, or it can be added manually
with the `wsPaneTitle` fragment.

The title typically contains a name and at least one action which hides the area the pane is in.

Other actions can be specified by setting `WorkspacePane.actions`.

## Workspace state

The `Workspace` class stores the state of the workspace. Typically, there is a declaration of a
workspace somewhere in an adaptive function:

```kotlin
@Adaptive
fun siteMain() {

    val workspace = buildWorkspace()

    localContext(workspace) {
        wsFull(workspace)
    }

}
```

The state variable `workspace` typically never changes, instead it contains adaptive value stores
fragments can use to manage changes.

The `localContext` fragment makes it possible for all inner fragments to find the workspace instance:

```kotlin
@Adaptive
fun someFun() {
    val workspace = firstContext<Workspace>()
}
```

### Horizontal states

It is a common requirement to be able to share some of the state of a given tool, so other tools and/or editors
can modify it and the tool can act accordingly.

The workspace calls these "horizontal states" contexts and stores them in `Workspace.contexts`. They
are horizontal because when fragments use these they reach over the fragment tree horizontally to access/modify
the state of another fragment.

You can add any kind of context to the workspace:

```kotlin
fun Workspace.cookbookCommon() {
    contexts += CookbookContext(this)
}
```

And then look up that context with `wsContext`:

```kotlin
@Adaptive
fun cookbookCenter(): AdaptiveFragment {
    val recipeKey = valueFrom { fragment().wsContext<CookbookContext>().activeRecipeKey }

    box {
        maxSize .. padding { 16.dp } .. verticalScroll

        if (recipeKey != null) {
            actualize(recipeKey)
        }
    }

    return fragment()
}
```

In this case `CookbookContext.activeRecipeKey` is an Adaptive value store. Whenever the value in the
store changes, a new value for `recipeKey` is generated and the normal patching mechanism will take care
of the rest.
