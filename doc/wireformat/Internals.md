# WireFormat Internals

WireFormat is optimized for serializing function call arguments and their return values. It can be easily used
for general purpose serialization but that's not the main goal.

It is used at these points:

* encode the function arguments of service calls
* decode the function arguments for service implementation `dispatch`
* encode the return value of `dispatch`
* decode the return value of the service call

Encode the service call parameters and decode the return value:

```kotlin
suspend fun testFun(arg1: Int, arg2: String): String =
    wireFormatDecoder(
        serviceCallTransport.call(
            "testFun",
            wireFormatEncoder
                .int(1, "arg1", arg1)
                .string(2, "arg2", arg2)
        ).asInstance(StringWireFormat)
    )
```

Decode `dispatch` arguments and encode the return value:

```kotlin
fun dispatch(payload: Message): ByteArray {
    return wireFormatEncoder.rawInstance(
        testFun(
            payload.int(1, "arg1"),
            payload.string(2, "arg2")
        ),
        StringWireFormat
    )
}
```


## OLD STUFF - ALL LIES

Cases depending on the value to encode/decode:

| Case                      | Handling                                                                         |
|---------------------------|----------------------------------------------------------------------------------|
| primitive                 | Call the appropriate primitive method.                                           |
| primitive array           | Call the appropriate primitive list method.                                      |
| enum                      | Call the `*enum*` method with a parameter that contains the enum entries.        |
| instance - not open class | Call the `*instance*` method with a parameter that implements `WireFormat`.      |
| instance - open class     | Call the `*polymorphic*` method with a parameter that specifies the actual type. |
| ArrayList - primitive     | Call the appropriate primitive list method.                                      |
| ArrayList - enum          | Call the `*enumList*` method with a parameter that contains the enum entries.    |
| ArrayList - instance      | Call the `*instanceList*` method with a parameter that implement `WireFormat`.   |
| collection - primitive    | Call the `*polymorphic*` method with a parameter that specifies the actual type. |
| collection - enum         | Call the `*enumList*` method with a parameter that contains the enum entries.    |
| collection - instance     | Call the `*instanceList*` method with a parameter that implement `WireFormat`.   |

Primitives:

| Type                             |
|----------------------------------|
| kotlin.Unit                      |
| kotlin.Boolean                   |
| kotlin.Int                       |
| kotlin.Short                     |
| kotlin.Byte                      |
| kotlin.Long                      |
| kotlin.Float                     |
| kotlin.Double                    |
| kotlin.Char                      |
| kotlin.String                    |
| kotlin.UInt                      |
| kotlin.UShort                    |
| kotlin.UByte                     |
| kotlin.ULong                     |
| hu.simplexion.z2.utility.UUID<T> |

## Instances

Instances of classes are encoded/decoded with the `instance` function:

```kotlin
// MessageEncoder
fun instance(fieldNumber: Int, fieldName: String, instance: T, wireFormat: WireFormat<T>) {}

// MessageDecoder
fun instance(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>) {}
```

### Polymorphism

A type is polymorphic when it is a class that implements `WireFormat` and it is `open`.

```kotlin
open class A : WireFormat
class B : A(), WireFormat
class C : A(), SuperWireFormat

fun t1(a: A) {} // polymorphic function call, uses A's (for A and C) or B's companion
fun t2(b: B) {} // non-polymorphic function call, uses B's companion
fun t3(c: C) {} // non-polymorphic function call, uses A's companion
```

`WireFormatDecoder.instance` decides which `WireFormat` implementation to use by these rules:

- from the companion of the actual instance's class, if it implements `WireFormat`
- from the supertype class's companion, if the actual instance implements `SuperWireFormat`
- otherwise an exception is thrown during compilation time

The type information is provided by adding the fully qualified name of the actual class as a string field.

For Protobuf this is a field with field number 30000.
For JSON this is a field named `$type` in the object that belongs to the instance.

The `WireFormat` of the open class has to handle the polymorphism by looking up the actual class
in the wireformat registry.

```kotlin
//@formatter:off
class WireFormatRegistry {
    val formats = mutableMapOf<String, WireFormat<*>>()

    fun decodeInstance(wireFormatDecoder: Message, type: String) =
        checkNotNull(formats[type]) { "missing wire format for $type" }.decodeInstance(wireFormatDecoder)
}

open class A : WireFormat<T> {

    companion object : WireFormat<T> {

        override val fqName
            get() = "test.A"

        override fun encodeInstance(encoder: MessageBuilder, value: T): MessageBuilder =
            builder // call appropriate methods to encode the instance fields

        override fun decodeInstance(wireFormatDecoder: Message): A {
            val actualFqName = wireFormatDecoder.string(3000, "\$type")
            if (actualFqName == this.fqName) {
                return A().apply { /* set properties from wireFormatDecoder fields */ }
            } else {
                return formats.decodeInstance(wireFormatDecoder, actualFqName)
            }
        }
    }
  
}
//@formatter:on
```

## Collections

For collections the general rule is:

* if the passed instance implements `WireFormat`
    * decode uses the exact same class as the encoded instance
    * encode/decode function of the class is used (from companion)
* if the instance does not implement `WireFormat`
    * decode uses the standard Kotlin data type from `kotlin.collections`

```kotlin
// MessageBuilder

fun intCollection(fieldNumber: Int, fieldName: String, instance: Collection<Int>) {
    intCollection(fieldNumber, fieldName, false, false, instance)
}

fun intCollection(fieldNumber: Int, fieldName: String, nullable: Boolean, itemNullable: Boolean, instance: Collection<Int?>) {

}

// Message

fun intCollection(fieldNumber: Int, fieldName: String, instance: MutableCollection<Int>) {
    intCollection(fieldNumber, fieldName, false, false, instance)
}

fun intCollection(fieldNumber: Int, fieldName: String, nullable: Boolean, itemNullable: Boolean, instance: MutableCollection<Int?>) {

}
```
