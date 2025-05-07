---
language: kotlin
tags: [core, adat]
title: Adat Class Creation
---

## Summary

This implementation adds an Adat class with the specified fields.

---

## Objective

Write code for a new Adat class with the following parameters:

- name: ExampleData
- module: fun.adaptive.example.model
- fields:
  - `exampleString` 
    - type: String
    - nullable: true
    - default: null
  - `exampleInt`
    - type: Int
    - nullable: false
  - `exampleList`
    - type: list of Double
    - nullable: false

---

## Steps

### 1. Write the code of the class

**File**: `src/commonMain/kotlin/fun/adaptive/example/model/ExampleData.kt`

### Purpose

Defines a data model with 

### Code

```kotlin
package `fun`.adaptive.example

import `fun`.adaptive.adat.Adat

@Adat
class ExampleData(
    val exampleString: String? = null,
    val exampleInt : Int,
    val exampleList : List<Double>
)
```

### Explanation

This class defines the data model of `ExampleData`. During compilation the Adaptive compiler plugin
enriches this definition, so frequently used functions are ready to use out-of-the-box.

---

# See Also

- [Adat Class Introduction](adat_class_introduction_guide.md)
- [Adat Functions](adat_functions_guide.md)
- [Adat Metadata](adat_metadata_guide.md)
