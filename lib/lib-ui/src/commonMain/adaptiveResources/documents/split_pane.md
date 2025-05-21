# Split Pane

Split an area horizontally or vertically.

---

## Hard-coded examples

[Split pane example](actualize:///cookbook/example/split-pane-proportional)

[Wrapper example](actualize:///cookbook/example/split-pane-wrapper)

---

## Playground

---

## Details

Split panes have a view backend that stores the current state of the split pane:

```kotlin
val backend = copyOf { splitPaneBackend(SplitVisibility.Both, SplitMethod.FixFirst, 100.0, Orientation.Horizontal) }

splitPane(
    backend,
    { text("pane1") },
    { verticalSplitDivider() },
    { text("pane2") }
)
```

### Visibility

Defines which pane of the two should be shown:

* `None`
* `First`
* `Second`
* `Both`

### Split method

Defines the space distribution method:

| Method         | Mechanism                                                                          |
|----------------|------------------------------------------------------------------------------------|
| `FixFirst`     | The first pane is fixed size, the second occupies the remaining space.             |
| `FixSecond`    | The second pane is fixed, the first occupies the remaining space.                  |
| `Proportional` | The available space is distributed between the panes according to the split value. |
| `WrapFirst`    | The available space is distributed as if the first pane would wrap the second.     |
| `WrapSecond`   | The available space is distributed as if the second pane would wrap the first.     |

### Wrapping

Wrapping means that the size of a fragment depends on the size of another fragment and should be
laid out accordingly. 

For example, the quotes in Markdown have a decoration which should be as high as the quote content.
We do not know the height of the quote content before actually rendering it as it depends on the
available width. The available width depends on the width of the decoration therefore, we have
a cyclic dependency.

To solve this, we have to decide the width of the wrapping fragment first, then lay out the wrapped
fragment, then lay out the wrapping fragment with the height of the wrapped fragment.

Split pane handles this process when the method is one of `Wrap*`.

There are four shortcuts for wrapping:

* `wrapFromLeft`
* `wrapFromRight`
* `wrapFromTop`
* `wrapFromBottom`

These handle the split pane view backend configuration under the hood:

```kotlin
val wrapperWidth = 8.dp

@Adaptive
fun someFun() {
    wrapFromLeft(wrapperWidth, ::leftWrapper) {
        text("Hello World!")
    }
}

@Adaptive
private fun leftWrapper(): AdaptiveFragment {
    box { maxHeight .. width { wrapperWidth } .. backgrounds.successSurface .. cornerRadius { 2.dp } }
    return fragment()
}
```

### Split value

The percentage or the size of the panes.

* when the method is `Propotional` it is the ratio between the first and the second pane
* when the method is `Fix*`, it is the size of the fix pane (DPixel)
* when the method is `Wrap*`, it is the size of the wrapping pane (DPixel)

### Orientation

* `Horizontal` - panes next to each other
* `Vertical` - one pane below the other

---

## Internals

Split pane is a [platform-dependent](def://) fragment because it is a container fragment that 
implies receiver dependency.

The common `AbstractSplitPane` contains everything, the [platform-dependent](def://) implementation
only has to extend this class as `AuiSplitPane`.