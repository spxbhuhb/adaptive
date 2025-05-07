---
title: Adat Functions Guide
tags: [adat, kotlin, serialization, guide]
language: kotlin
---

# Summary

This guide describes how to use **Adat** class functions in the Adaptive platform. 
Adat classes are enhanced data models with built-in metadata, validation, runtime context,
and serialization capabilities.

---

# Objective

Explain how to use the Adat class functions.

---

# Steps

## 1. Copy and Compare Instances

### Purpose

Creates a shallow copy of an Adat class instance.

### Code

```kotlin
val original = ExampleData(12, false)

val copy1 = original.copy()
val copy2 = original.copy(exampleInt = 23)

check(copy1 == original)
check(copy2 != original)
```

### Explanation

This demonstrates shallow copy behavior. The `copy()` method behaves like Kotlin's `data` class copy, supporting property overrides.

---

## 2. Diff Between Instances

### Purpose

Calculate the difference between two Adat class instances. The instances do not have to be
the same class.

### Code

```kotlin
val original = ExampleData(12, false)
val copy = original.copy(exampleInt = 23)

val diff = original.diff(copy)
check(diff.isNotEmpty())
check(diff[0].kind == AdatDiffKind.ValueDiff)
```

### Explanation

The `diff()` function returns a list of differences between two Adat instances based
on property names.

The list contains `AdatDiffItem` instances:

```kotlin
class AdatDiffItem(
    val kind : AdatDiffKind,
    val path : String,
    val index : Int?
)

enum class AdatDiffKind {
    IndexDiff, // properties with the same name have different index
    SignatureDiff, // properties with the same name have different signature
    ValueDiff, // properties with the same name have different value
    MissingFromThis, // a property is missing from "this instance
    MissingFromOther // a property is missing from "other" instance
}
```

---

## 3. Setting Values by Name or Index

### Purpose

Without having access to the actual class itself (such as for no-code classes) set the value of
properties based on name or index.

### Code 

```kotlin
copy1.setValue(1, false)             // by index
copy1.setValue("exampleBoolean", true) // by name
```

### Explanation

The `setValue()` function modifies properties either by index or property name. 
It throws an exception if trying to set an immutable property.

---

## 4. Getting Values by Name or Index

### Purpose

Without having access to the actual class itself (such as for no-code classes) get the value of
properties based on name or index.

### Code

```kotlin
val value1 = copy1.getValue(0)
val value2 = copy1.getValue("exampleInt")

check(value1 == 12)
check(value2 == 12)
```

### Explanation

`getValue()` allows reading property values dynamically.

---

## 5. Accessing Metadata

### Purpose

Metadata of Adat classes can be used for many purposes such as reflection,
automatic validation, etc.

### Code

```kotlin
val metadataFromCompanion = ExampleData.adatMetadata
val metadataFromInstance = original.getMetadata()
```

### Explanation

Adat class metadata provides a concise, platform-independent way to store the data model
of an Adat class. This enables many utility functions to work without knowing the exact
class and/or even with no-code data instances.

Use `adatMetadata` of the companion for or `getMetadata()` of an actual instance to get
the metadata.

For more information about the metadata, see [Adat Metadata Guide](adat_metadata_guide.md).

---

## 6. Converting to and from Arrays

### Purpose

Array conversions make export and import of property value possible.

### Code

```kotlin
val arrayOfPropertyValues = original.toArray()
val copy3 = ExampleData.newInstance(arrayOfPropertyValues)

check(copy3 == original)
```

### Explanation

For serialization and for no-code classes, it is necessary to have a constructor with
a known, model-independent signature and a function to access that constructor:

- `toArray()` converts all properties into an array
- `newInstance()` reconstructs an instance from such an array

---

# Notes

- **Adat** processes only primary constructor properties.
- Companion objects include deserialization and instantiation logic.

---

# See Also

- [Adat Class Introduction](adat_class_introduction_guide.md)
- [Adat Class Creation](adat_class_creation_guide.md)
- [Adat Metadata](adat_metadata_guide.md)

---

# Conclusion

The utility functions provided by Adat classes extend data model behavior far 
beyond what standard Kotlin `data` classes offer. With support for deep comparison,
dynamic field access, metadata introspection, and structural conversion, Adat classes 
become powerful primitives for reactive systems, schema-driven UIs, and transport-safe APIs. 
Mastering these functions unlocks the full potential of Adaptive’s declarative, 
metadata-rich architecture.