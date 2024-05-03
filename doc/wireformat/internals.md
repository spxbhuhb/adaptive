# Internals

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