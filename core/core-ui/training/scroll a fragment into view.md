---
language: kotlin
tags: [ui, layout, fragment, scroll]
---

## Objective
Scroll a fragment into view when it is displayed.

## Solution
```kotlin
afterPatchBatch { scrollIntoView(it) }
```

## Explanation
`afterPatchBatch` executes the code passed to it when the first layout of the fragment
has been done.

The `scrollIntoView` utility function scrolls the first UI fragment into view by
calling `AbstractAuiAdapter.scrollIntoView`.

### Problem
Scroll a fragment into view when it is displayed based on a condition.

### Solution
```kotlin
if (condition) {
    afterPatchBatch { scrollIntoView(it) }
}
```