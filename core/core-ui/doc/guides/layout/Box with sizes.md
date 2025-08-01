---
status: planning
---

# Box with sizes

A box which defers building its content until the layout [sizing proposal](def://) is available.

During layout each [UI fragment](def://) gets a [sizing proposal](def://) that contains
the minimum and maximum space available for the fragment.

Layouts and fragments usually handle this proposal automatically, but there are situations
when a fragment wants to arrange its components based on the available space.

[boxWithSizes](fragment://) provides the sizing proposal to the content building function,
so the content can be built according to the available space.

```kotlin
fun boxWithSizesExample() {
    
    boxWithSizes { proposal -> 
        column {
            text("proposed minimum width: ${proposal.minWidth}")
            text("proposed maximum width: ${proposal.maxWidth}")
            text("proposed minimum height: ${proposal.minHeight}")
            text("proposed maximum height: ${proposal.maxHeight}")
        }
    }
    
}
```