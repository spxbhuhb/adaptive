---
language: kotlin
tags: [ui, layout, fragment, callback]
---

### Problem
In an Adaptive UI fragment, execute some code after the first layout.

### Solution
```kotlin
afterPatchBatch { declaringFragment ->
    // code to execute
}
```

### Explanation
Layout updates are executed by `AbstractAuiAdapter.closePatchBatch` when the
patch batch is processed.

The `afterPatchBatch` fragment schedules the parameter function to run by
`AbstractAuiAdapter.closePatchBatch`.

`afterPatchBatch` parameter `once` is `true` by default, meaning that the code
will be executed only once, after the first layout.

### Problem
In an Adaptive UI fragment, execute some code after each layout update.

### Solution
```kotlin
afterPatchBatch(once = false) { declaringFragment ->
    // code to execute
}
```

### Explanation
Layout updates are executed by `AbstractAuiAdapter.closePatchBatch` when the
patch batch is processed.

The `afterPatchBatch` fragment schedules the parameter function to run by
`AbstractAuiAdapter.closePatchBatch`.

> [!DANGER]
> 
> When the parameter `once` is false, changing any state variables in the function that the 
> function depends on **AND** triggers a re-layout will launch an infinite loop.
>