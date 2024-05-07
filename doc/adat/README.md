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

These classes introduce some computational overhead, but they provide many utility functions out-of-the-box:

* they are WireFormat capable by default (can be serialized, transmitted over network, used in service calls, etc.)
* they also provide nice utility functions:
  * `copy` - same as for standard Kotlin data classes
  * `equals` - same as for standard Kotlin data classes
  * `hashCode` - same as for standard Kotlin data classes
  * `deepCopy` - like `copy` but it creates a new copy of each array, collection and `@Sign` class as well
  * `diff` - calculates the difference between two `@Sign` classes
  * `setValue` - set a value based on its name or its index
  * `getValue` - get a value based on its name or its index
  * `getCompanion` - get the companion object of this instance
  * `getMetadata` - get metadata of the class
  * `adatInstance` - get a new instance (from Companion)


All these functions are **NOT** generated for these classes, they are implemented in `AdatClass` and
`AdatCompanion`.

What **IS** generated:

* an `adatValues` and an `adatCompanion` property
* a companion object with `adatMetadata`, `adatWireFormat` properties and a `newInstance` function
* property getters and setters for the declared properties

Check [AdatTest](/adaptive-core/src/commonTest/kotlin/hu/simplexion/adaptive/adat/AdatTest.kt) for details.