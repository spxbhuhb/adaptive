# Adat metadata

[adat metadata](def://?inline)

Adat metadata enables runtime reflection, schema-based UI generation, and transport-safe serialization.
It abstracts the structure of a class into a compact, JSON-encoded format accessible at runtime.

For every Adat class, the Adaptive compiler plugin generates a companion object containing metadata.

The metadata is accessible by:

- The `adatMetadata` property of the companion object.
- The `getMetadata()` function of any Adat instance (which returns the `adatMetadata` of the companion).

Runtime types: `AdatClassMetadata` and `AdatPropertyMetadata`.

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

## Flags

### Class Flags

- `1` – IMMUTABLE: the class instances are immutable

### Property Flags

- `1` – VAL: declared with `val`
- `2` – IMMUTABLE_VALUE: the property value is immutable
- `4` – ADAT_CLASS: value is another Adat class
- `8` – NULLABLE: value can be null
- `16` – HAS_DEFAULT: the property has a default value

Descriptors (when present) enable validation and UI behavior.