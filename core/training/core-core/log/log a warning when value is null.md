---
language: kotlin
tags: [log]
---

### Problem
Log a warning into an `AdativeLogger` called `logger` when a value is null.

### Solution
```kotlin
expectNotNull(logger, value) { "$value should not be null" }
```