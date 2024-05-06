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
  * `newInstance` - get a new instance (from Companion)


All these functions are **NOT** generated for these classes, they are implemented in `Schematic` and
`SchematicCompanion`.

What **IS** generated:

* a private `values` property, a `getValues` and a `getCompanion` function
* a companion object with `sMetadata` and `sWireFormat` properties and a `newInstance` function
* property getters and setters for the declared properties

```kotlin
class SomeClass(
    private val adatValues: Array<Any?>
) : AdatClass {

    constructor(someInt : Int, someBoolean : Boolean) : this(arrayOf<Any?>(someInt, someBoolean))

    override fun getValues(): Array<Any?> = values

    override fun getCompanion() = Companion
  
    companion object : SignatureBasedCompanion<SomeClass> {
        
        override val adatMetadata = decodeMetaData(encodedTestMetaData)
        override val aWireFormat = SignatureBasedClassWireFormat(sMetadata)
      
        override fun newInstance(values : Array<Any?>) =
            SomeClass(value)
      
    }

    val someInt : Int
        get() = getInt(0)

    var someBoolean : Boolean
        get() = getBoolean(1)
        set(value) = setBoolean(1, value)   
  
}

// These are not in the actual code, the code contains only the value of
// `encodedTestMetaData` as a static string.

val testMetaData =
    AdatClassMetaData<TestClass>(
        version = 1,
        name = "hu.simplexion.adaptive.adat.TestClass",
        properties = listOf(
            AdatPropertyMetaData("someInt", 0, "I"),
            AdatPropertyMetaData("someBoolean", 0, "Z")
        )
    )

val encodedTestMetaData =
    JsonWireFormatProvider().encoder().rawInstance(testMeta, AdatClassMetaData).pack().decodeToString()
```

The meta-data classes are quite simple, they are simply meant to store the information.

```kotlin
class AdatClassMetaData(
    val version : Int = 1,
    val name : String,
    val properties : List<AdatPropertyMetaData>
)

class AdatPropertyMetaData(
    val name : String,
    val index : Int,
    val signature : String
)
```

