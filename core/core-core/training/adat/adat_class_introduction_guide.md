---
title: Adat Class Introduction
tags: [adat, kotlin, serialization, metadata, guide]
language: kotlin
---

# Summary

This guide describes how to define and use **Adat classes** in the Adaptive platform. 
Adat classes are enhanced data models with built-in metadata, validation, runtime context,
and serialization capabilities.

---

# Objective

Explain how to use the `@Adat` annotation to create data classes that support:

- Compile-time and runtime metadata
- WireFormat serialization
- Validation and descriptor-based constraints
- Field-level access by name or index
- Utility functions such as `diff`, `deepCopy`, and `toArray`

---

# Steps

## 1. Declaring an Adat Class

**File**: `src/commonMain/kotlin/fun/adaptive/example/ExampleData.kt`

### Purpose

Defines an Adat class for the data model `ExampleData`.

### Code

```kotlin
package `fun`.adaptive.example

import `fun`.adaptive.adat.Adat

@Adat
class ExampleData(
    val exampleInt : Int,
    var exampleBoolean : Boolean
)
```

### Explanation

Adat classes are annotated with the `@Adat` annotation. This instructs the Adaptive compiler
plugin to process the class definition.

As for a standard Kotlin `data` class, only primary constructor properties are considered, 
properties declared in the class body are ignored. This ensures predictable metadata and
serialization behavior. 

The compiler plugin generates support code for each `@Adat` class:

Instance:

- `adatCompanion` - stores the companion object that belongs to an actual instance
- `adatContext` - stores a context (such as a form) the instance belongs to
- `genGetValue` - get property values by index
- `genSetValue` - set property values by index

`gen*` functions are used internally by the Adaptive runtime and typically accessed via getValue/setValue wrappers.

Companion Object:

- `adatMetadata` - a string that contains an `AdatClassMetadata` encoded into JSON
- `adatWireFormat` - the wire-format used for serialization and deserialization
- `adatDescriptors` - class and property descriptors used for validation and UI hints
- `wireFormatName` - the fully qualified name of the class
- `newInstance(values: Array<Any?>)` - a function to create a new instance from an array of values

These support serialization, dynamic instantiation, and metadata retrieval.

---

# Notes

- **Adat** processes only primary constructor properties.
- Companion objects include deserialization and instantiation logic.

---

# See Also

- [Adat Class Creation](adat_class_creation_guide.md)
- [Adat Functions](adat_functions_guide.md)
- [Adat Metadata](adat_metadata_guide.md)

---

# Conclusion

Adat classes combine the simplicity of Kotlin data classes with powerful runtime capabilities like metadata, 
context, and wire-format serialization. They are a foundational element in Adaptive-based apps for
data transport, validation, and transformation.