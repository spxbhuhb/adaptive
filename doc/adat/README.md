# Adaptive Data Classes

A class is an adaptive data class when it has the `@Adat` annotation:

```kotlin
package somepackage

@Adat
class SomeClass(
    val someInt : Int,
    var someBoolean : Boolean
)
```

These classes are similar to `data` classes in concept but offer many other functions in addition:

* metadata (a.k.a. reflection) available during runtime, including
  * property name, type, index
  * constraints (if provided)
  * additional information (if provided)
* validation
* automatic mapping to Exposed tables
* get/set fields by name or index
* serializable (with WireFormat)
* sensible defaults (when not provided directly)
* data arithmetics (or, and, etc.) - haven't finished the implementation yet
* nice utility functions:

| Function      | Status        | What it does                                                                              |
|---------------|---------------|-------------------------------------------------------------------------------------------|
| `copy`        | ready         | make a copy of an instance                                                                |
| `equals`      | ready         | same as for standard Kotlin data classes                                                  |
| `hashCode`    | ready         | same as for standard Kotlin data classes                                                  |
| `deepCopy`    | to be written | like `copy` but it creates a new copy of each array, collection and `@Adat` class as well |
| `diff`        | ready         | calculates the difference between two `@Adat` classes                                     |
| `setValue`    | ready         | set a value based on its name or its index                                                |
| `getValue`    | ready         | get a value based on its name or its index                                                |
| `getMetadata` | ready         | get metadata of the class                                                                 |
| `newInstance` | ready         | get a new instance (from Companion)                                                       |

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
    * `wireFormatName`
  * functions:
    * `newInstance()`
    * `newInstance(values : Array<Any?>)`

Check [AdatTest](/adaptive-core/src/commonTest/kotlin/hu/simplexion/adaptive/adat/AdatTest.kt) for details.

## Description

> [!NOTE]
>
> As of now, complex constraints are not supported. They are not particularly complex to add, but I don't need
> them right now, and there is so much to do.
>
> Also, the available descriptors are quite limited for now, I'll add them as needed.
>

With the `description` function you can describe the constraints of the data or provide additional information.

```kotlin
@Adat
class TestAdat(
  var someInt: Int,
  var someBoolean: Boolean,
  var someIntListSet: Set<List<Int>>
) {

  init {
    description {
      someInt minimum 5 maximum 10 default 7
      someBoolean default true
      someIntListSet default setOf(listOf(12, 13))
    }
  }

}
```

## Metadata

The `getMetadata()` function returns with the metadata of the given Adat instance. It is stored in the
`adatMetadata` property of the companion object of each Adat class.

The metadata is an instance of [AdatClassMetadata](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/adat/metadata/AdatClassMetadata.kt).

During compilation the compiler builds the metadata of the class and serializes it into JSON. This JSON is
deserialized when the companion object is initialized.

An example metadata:

```json
{
  "v": 1,
  "n": "hu.simplexion.adaptive.adat.TestClass",
  "p": [
    {
      "n": "someInt",
      "i": 0,
      "s": "I",
      "d": []
    },
    {
      "n": "someBoolean",
      "i": 1,
      "s": "Z",
      "d": []
    },
    {
      "n": "someIntListSet",
      "i": 2,
      "s": "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;",
      "d": []
    }
  ]
}
```