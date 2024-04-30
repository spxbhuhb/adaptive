# Protobuf

## Null Values

Null values are encoded as booleans with `true` value, but the field number is increased by 20000.

```kotlin
fun a(i1 : Int?) {}
```

```protobuf
message A {
  optional sint32 i1 = 1;
  optional bool i1IsNull = 20001;
}
```

When decoding, the decoder first checks if there is a record with the increased number, if so returns with `null`.
Otherwise, checks the record with the original field number (and throws an exception if not found).

## Standalone Values

Standalone values (function return values and items of collections) are encoded with field number `1` or field number
`20001` in case of nullable values.

## Collections

WireFormat's Protobuf implementation encodes Kotlin collections the same way as instances. This is results in valid
Protobuf messages, but it does not support direct, inlined Protobuf lists:

If we take this function as an example:

```kotlin
fun a(i1 : List<Int>) {}
```

Technically the list can be included in the message that describes the arguments of the function.

```protobuf
message A {
  repeated sint32 i1 = 1;
}
```

However, this approach has a few drawbacks:

- complicates the message encoders and decoders
- a large list makes finding the records by tag expensive

Thereforw, WireFormat defines two implicit Protobuf messages, one for the arguments of the function and one for the list itself:

```protobuf
message A {
  required AI1 i1 = 1;
}

message AI1 {
  repeated sint32 item = 1;
}
```

