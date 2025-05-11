# Event Instructions

## Pointer events

These cover mouse and tap events as well. For browser, `onMove` can happen when there is no previous down event,
on mobile there should be a down event before `onMove`.

```kotlin
box(maxSize) {
    onClick { println("Click ${it.x} ${it.y}") }
}
```

* `onClick`
* `onMove`
* `onPrimaryDown`
* `onPrimaryUp`
* `onSecondaryDown`
* `onSecondaryUp`

All pointer event handlers receive a `UIEvent` as parameter:

```kotlin
class UIEvent(
    val fragment: AbstractCommonFragment<*>,
    val nativeEvent: Any?,
    val x: Double = Double.NaN,
    val y: Double = Double.NaN
)
```

`x` and `y` are **RAW** coordinates, relative to `fragment`.
