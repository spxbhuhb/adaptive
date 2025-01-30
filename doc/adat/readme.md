# Adaptive Data Classes

A class is an adaptive data class when it has the `@Adat` annotation:

```kotlin
@Adat
class SomeClass(
    val someInt : Int,
    var someBoolean : Boolean
)
```

> [!IMPORTANT]
> 
> Just as for data classes, only the properties declared in the primary constructor are
> handled by the generated code. Properties declared in the class body are ignored.
> 

Subtopics:

* [Descriptor and validation](descriptor-and-validation.md)
* [Immutable adat classes](immutable-adat-classes.md)
* [Integration with Exposed](exposed.md)

These classes are similar to `data` classes in concept but offer many other functions in addition:

* metadata (a.k.a. reflection) available during runtime, including
  * property name, type, index
  * immutability information
  * constraints (if provided)
  * additional information (if provided)
* runtime context (optional, used for bindings, error reports etc.)
* validation
* automatic mapping to Exposed tables
* get/set fields by name or index
* serializable (with WireFormat)
* utility functions:

| Function      | What it does                                                                                                                         |
|---------------|--------------------------------------------------------------------------------------------------------------------------------------|
| `copy`        | make a copy of an instance                                                                                                           |
| `equals`      | same as for standard Kotlin data classes, but metadata based                                                                         |
| `hashCode`    | same as for standard Kotlin data classes, but metadata based                                                                         |
| `deepCopy`    | like `copy` but it creates a new copy of each array, collection and `@Adat` class as well<br>recognizes and optimizes immutable data |
| `diff`        | calculates the difference between two `@Adat` classes                                                                                |
| `setValue`    | set a value based on its name or its index                                                                                           |
| `getValue`    | get a value based on its name or its index                                                                                           |
| `getMetadata` | get metadata of the class                                                                                                            |
| `newInstance` | get a new instance (from Companion)                                                                                                  |
| `toArray`     | export all properties into an array                                                                                                  |
| `touch`       | mark properties touched (when the user changes the value)                                                                            |

Most of these functions are **NOT** generated for these classes, they are implemented in `AdatClass`,
`AdatCompanion` or by extension functions.

What **IS** generated:

* properties
  * `adatCompanion`
  * `adatContext`
* functions:
  * `genGetValue`
  * `genGetValue`
* a companion object
  * properties:
    * `adatMetadata`
    * `adatWireFormat`
    * `adatDescriptors`
    * `wireFormatName`
  * functions:
    * `newInstance(values : Array<Any?>)`

Check [AdatTest](/adaptive-core/src/commonTest/kotlin/fun/adaptive/adat/AdatTest.kt) for details.

## Metadata

The `getMetadata()` function returns with the metadata of the given Adat instance. It is stored in the
`adatMetadata` property of the companion object of each Adat class.

The metadata is an instance of [AdatClassMetadata](/adaptive-core/src/commonMain/kotlin/fun/adaptive/adat/metadata/AdatClassMetadata.kt).

During compilation the compiler builds the metadata of the class and serializes it into JSON. This JSON is
deserialized when the companion object is initialized.

```kotlin
@Adat
class TestAdat(
  val someInt: Int,
  var someBoolean: Boolean,
  val someIntListSet: Set<List<Int>>
)
```

Metadata of the class above:

* v = version
* n = fully qualified name of the class
* f = flags
  * for classes `1` means the class is immutable
  * for properties
    * `1` - the property is `val` without getter
    * `2` - the property value is an immutable class or an immutable primitive
    * `3` - `1 + 2`
    * `4` - the property value is an adat class
* i = index of the property
* d = description (constraints and additional information, see [Descriptor and validation](descriptor-and-validation.md)).

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