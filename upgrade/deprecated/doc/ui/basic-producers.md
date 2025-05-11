# Basic Producers

## mediaMetrics

This producer can be used to get information about the current media.

With `mediaMetrics` you can make your code optimized for different screens easily:

```kotlin
@Adaptive
fun mediaSize() {
    val media = mediaMetrics()
    val screenSize = "${media.viewWidth} x ${media.viewHeight}"

    when {
        media.isSmall -> text("small screen ($screenSize)")
        media.isMedium -> text("medium screen ($screenSize)")
        media.isLarge -> text("large screen ($screenSize)")
    }

}
```

## hover

Produces:

- `true` if the mouse hovers over any of the children of the fragment
- `false` when the mouse is outside all children of the fragment

```kotlin
@Adaptive
fun hoverMain() {
    val hover = hover()

    column {
        box {
            width { 32.dp } .. height { 32.dp }
            if (hover) backgroundColor(0xff0000) else backgroundColor(0x00ff00)
        }

        if (hover) text("hover")
    }

}
```