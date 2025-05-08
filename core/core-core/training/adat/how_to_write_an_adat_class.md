---
title: How to Write an Adat Class
tags: [core, adat]
type: procedural
---

## Summary

This implementation adds an Adat class with the specified fields.

## Objective

Create a new Adat class named `ExampleData` in the `my.project.example.model` package. 

The class should include:

* exampleString: nullable String, default value null
* exampleInt: non-nullable Int
* exampleList: non-nullable list of doubles

## Steps

### 1. Write the code of the class

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

# Notes

- Only fields in the primary constructor are processed by the `@Adat` annotation.

# See Also

- [What Is an Adat Class](what_is_an_adat_class.md)
- [What Are the Adat Functions](what_are_the_adat_functions.md)
- [What Is the Adat Metadata](what_is_the_adat_metadata.md)

## Conclusion

This demonstrates how to define an Adat class with multiple field types and nullability constraints.