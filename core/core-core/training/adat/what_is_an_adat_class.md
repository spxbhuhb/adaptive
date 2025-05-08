---
title: What is an Adat Class
tags: [adat, kotlin, serialization, metadata, guide]
type: conceptual
---

# Summary

This guide describes what an **Adat** class is in the Adaptive platform.
Adat classes are enhanced data models with built-in metadata, validation, runtime context,
and serialization capabilities.

# Objective

Understand what happens when the `@Adat` annotation is applied to the class, what Adat classes offer and how they behave.

# Key Concepts

An **Adat class** is a Kotlin class annotated with `@Adat`, enabling the Adaptive compiler plugin to
generate rich runtime capabilities. These include:

- serialization/deserialization,
- JSON-based metadata and validation descriptors,
- utility functions (`diff`, `deepCopy`, etc.),
- runtime context for embedding in forms or data flows,
- indexed access to fields via generated methods.

Only properties in the **primary constructor** are processed—this ensures consistent metadata
and behavior across compile-time and runtime.

# Components and Configuration

## Instance-Level Properties and Methods

Each `@Adat` instance includes:

- `adatCompanion` – references the companion metadata object
- `adatContext` – holds contextual runtime information (e.g. form)
- `genGetValue(index)` – retrieves value by index
- `genSetValue(index, value)` – sets value by index

These methods support reflective or dynamic usage of the class within the Adaptive runtime.

The `gen*` functions are primarily internal and accessed through higher-level getValue/setValue functions.

## Companion Object Features

The compiler also generates a static companion for each Adat class with:

- `adatMetadata`: Encoded metadata describing fields and constraints
- `adatWireFormat`: The wire format for serialization
- `adatDescriptors`: Descriptors for validation and UI generation
- `wireFormatName`: A globally unique string for class identification
- `newInstance(values: Array<Any?>)`: Factory method for dynamic instantiation

# Usage Example

```kotlin
package `fun`.adaptive.example.model

import `fun`.adaptive.adat.Adat

@Adat
class ExampleData(
    val exampleInt : Int,
    var exampleBoolean : Boolean
)
```

This example declares an Adat class with two properties. The generated code includes descriptors, serialization wiring,
and helper methods for runtime interaction.

# Notes

- Only primary constructor properties are considered. Class body properties are ignored.
- Adat classes are especially useful in environments with dynamic data requirements (e.g. forms, UI layers, validation).

# See Also

- [How to Write an Adat Class](how_to_write_an_adat_class.md)
- [What Are the Adat Functions](what_are_the_adat_functions.md)
- [What Is the Adat Metadata](what_is_the_adat_metadata.md)

# Conclusion

Adat classes combine the simplicity of Kotlin data models with powerful features like runtime
context, serialization, and metadata. They form a critical foundation in Adaptive applications
for structured, validated, and dynamic data handling.