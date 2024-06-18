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