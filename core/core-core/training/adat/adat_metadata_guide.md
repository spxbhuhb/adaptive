---
title: Adat Metadata Guide
tags: [adat, kotlin, serialization, metadata, guide]
language: kotlin
---

# Summary

This guide describes the metadata generated for **Adat** classes in the Adaptive platform. 
Adat classes are enhanced data models with built-in metadata, validation, runtime context,
and serialization capabilities.

---

# Objective

Explain Adat class metadata, how to access and use it.

---

## 1. Understanding Metadata

### Purpose

Adat class metadata provides a concise, platform-independent way to store the data model
of an Adat class. This enables many utility functions to work without knowing the exact
class and/or even with no-code data instances.

### Explanation

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

The metadata is stored in the `adatMetadata` property of the companion object of each Adat class.
The `getMetadata()` function call returns the metadata of the given Adat instance. 

During runtime the metadata is an instance of `AdatClassMetadata` and the properties are
instances of `AdatPropertyMetadata`.

Metadata Structure (JSON):

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

Class flags:

- 1 (IMMUTABLE) the instances of this class are immutable

Property flags:

- 1 (VAL) the property is a `val`
- 2 (IMMUTABLE_VALUE) the value of the property itself is immutable
- 4 (ADAT_CLASS) the value of the property is an Adat class
- 8 (NULLABLE) the property is nullable
- 16 (HAS_DEFAULT) the property has a default value
---

# See Also

- [Adat Class Introduction](adat_class_introduction_guide.md)
- [Adat Class Creation](adat_class_creation_guide.md)
- [Adat Functions](adat_functions_guide.md)

# Conclusion

Adat metadata enables powerful reflection, validation, and transformation without
requiring manual type inspection. By embedding this structured description into each class,
Adaptive applications gain robust serialization, UI generation, and runtime safety features.
Understanding the metadata model is essential for extending Adat functionality, debugging compatibility 
issues, or integrating with low-code and no-code tools.
