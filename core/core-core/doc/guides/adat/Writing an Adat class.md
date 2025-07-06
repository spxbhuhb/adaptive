# How to write an adat class

## 1. Write the code of the class

**File**: `src/commonMain/kotlin/my/project/example/model/ExampleData.kt`

### Purpose

Defines a data model class using @Adat to enable serialization, metadata, and runtime support.

### Code

```kotlin
package my.project.example

import `fun`.adaptive.adat.Adat

@Adat
class ExampleData(
    val exampleString: String? = null,
    val exampleInt : Int,
    val exampleList : List<Double>
)
```

### Explanation

This code defines a data model class named ExampleData annotated with @Adat, which will be processed 
by the Adaptive compiler to enable Adat features such as metadata, utility functions, etc.

## Notes

- Only fields in the primary constructor are processed by the `@Adat` annotation.