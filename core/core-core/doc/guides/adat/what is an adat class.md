# What is an Adat class

An **Adat class** is a Kotlin class annotated with `@Adat`, enabling the Adaptive compiler plugin to
generate rich runtime capabilities. These include:

- serialization/deserialization,
- JSON-based metadata and validation descriptors,
- utility functions (`diff`, `deepCopy`, etc.),
- runtime context for embedding in forms or data flows,
- indexed access to fields via generated methods.

Only properties in the **primary constructor** are processed—this ensures consistent metadata
and behavior across compile-time and runtime.

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