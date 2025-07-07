# Split Pane

Split an area horizontally or vertically.

[Split pane example](actualize://cookbook/example/split-pane-proportional)

## Details

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

`WrapSecond` and `WrapFirst` is used by [Wrap](guide://) fragments.

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