---
status: outdated
---

# Built-in layout instructions


>
> [!NOTE]
>
> Scroll is not implemented for Android and iOS yet.
>


```kotlin
column(alignItems.start)
column(alignItems.center)
column(alignItems.end)

column(alignItems.startCenter)
column(alignItems.topCenter)
column(alignItems.endCenter)
column(alignItems.bottomCenter)

column(scroll)
column(verticalScroll)
column(horizontalScroll)
```

### Scrolling

When adding scroll, pay attention to where you add the instruction.

This works, as you apply scroll to the column that contains the big stuff.

```kotlin
@Adaptive
fun a() {
    column {
        scroll
        bigStuff()
    }
}

@Adaptive
fun bigStuff() {
    box(size(10000.dp, 10000.dp)) {  }
}
```

This **DOES NOT WORK**. The reason is that the `box` is already big. It does not need
to be scrolled to contain everything. The column that contains `bigStuff` is small,
the content of that column has to be scrolled.

```kotlin
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.scroll

@Adaptive
fun a() {
    column {
        bigStuff()
    }
}

@Adaptive 
fun bigStuff() {
    box(size(10000.dp, 10000.dp)) {
        scroll
        throw RuntimeException("THIS DOES NOT WORK")
    }
}
```