---
status: planning
---

# Table

```kotlin
@Adaptive
fun stuff() {
    val backend = tableBackend {
        intColumn { it.i1 }
    }

    table(tableBackend)
}

@Adaptive
fun <T> table(
    backend : TableBackend<T>
) {
    // ...
    boxWithSizes { sizes : SizingProposal ->
        
    }
    // ...
}
```