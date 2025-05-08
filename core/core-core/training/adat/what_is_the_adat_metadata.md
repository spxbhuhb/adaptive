---
title: What Is the Adat Metadata
tags: [adat, metadata]
type: conceptual
---

# Summary

This guide describes the metadata generated for **Adat** classes in the Adaptive platform.
Adat classes are enhanced data models with built-in metadata, validation, runtime context,
and serialization capabilities.

# Objective

Understand how Adat metadata is structured, accessed, and used at runtime.

# Key Concepts

Adat metadata enables runtime reflection, schema-based UI generation, and transport-safe serialization.
It abstracts the structure of a class into a compact, JSON-encoded format accessible at runtime.

For every Adat class, the Adaptive compiler plugin generates a companion object containing metadata.

The metadata is accessible by:

- The `adatMetadata` property of the companion object.
- The `getMetadata()` function of any Adat instance (which returns the `adatMetadata` of the companion).

Runtime types: `AdatClassMetadata` and `AdatPropertyMetadata`.

# Components

## Accessing Metadata

Use either the companion object or an instance to retrieve metadata:

```kotlin
@Adat
class TestAdat(
  val someInt: Int,
  var someBoolean: Boolean,
  val someIntListSet: Set<List<Int>>
)

fun main() {
    val metadataFromCompanion = TestAdat.adatMetadata
    val metadataFromInstance = TestAdat(12, false, emptySet()).getMetadata()
}
```

## Structure of Metadata

At runtime, the metadata is represented by:

- `AdatClassMetadata`: holds the class-level structure
- `AdatPropertyMetadata`: holds property-level info

Metadata is encoded into JSON and looks like this:

```json
{
  "version": 1,
  "name": "fun.adaptive.adat.TestClass",
  "flags": 1,
  "properties": [
    {
      "name": "someInt",
      "index": 0,
      "flags": 3,
      "signature": "I",
      "descriptors": []
    },
    {
      "name": "someBoolean",
      "index": 1,
      "flags": 2,
      "signature": "Z",
      "descriptors": []
    },
    {
      "name": "someIntListSet",
      "index": 2,
      "flags": 3,
      "signature": "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;",
      "descriptors": []
    }
  ]
}
```

---

## Flags and Descriptors

### Class Flags

- `1` – IMMUTABLE: the class instances are immutable

### Property Flags

- `1` – VAL: declared with `val`
- `2` – IMMUTABLE_VALUE: the property value is immutable
- `4` – ADAT_CLASS: value is another Adat class
- `8` – NULLABLE: value can be null
- `16` – HAS_DEFAULT: the property has a default value

Descriptors (when present) enable validation and UI behavior.

# Notes

- Metadata is designed to be forward-compatible and stable across compiler/plugin versions.
- Descriptors are empty unless validation or UI hints are defined explicitly.

# See Also

- [What Is an Adat Class](what_is_an_adat_class.md)
- [How to Write an Adat Class](how_to_write_an_adat_class.md)
- [What Are the Adat Functions](what_are_the_adat_functions.md)

# Conclusion

Adat metadata enables powerful reflection, validation, and transformation without
requiring manual type inspection. By embedding this structured description into each class,
Adaptive applications gain robust serialization, UI generation, and runtime safety features.
Understanding the metadata model is essential for extending Adat functionality, debugging compatibility
issues, or integrating with low-code and no-code tools.