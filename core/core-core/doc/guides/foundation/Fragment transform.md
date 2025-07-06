# Fragment transform

[fragment transform](def://?inline)

[Fragments](def://) and consequently [fragment trees](def://) can be transformed with
[fragment transforms](def://). This is a rather advanced technique that lets [Adaptive](def://)
[applications](def://) change their behavior and/or [user interface](def://) during [runtime](def://).

To transform fragments, you need a class that performs the transformation. There are a few
built-in transformations, and it is also quite easy to write your own.

Built-in transforms:

- [InstructionReplaceTransform](class://) - replace an instruction with another

Base transform classes to extend:

- [FragmentTransformerVoid](class://)
- [FragmentTransformer](class://)

Once you have a transform class, you can easily transform a fragment by calling 
[transformChildren](function://AdaptiveFragment).

This code snippet shows how to rename all fragments called "hello" to "world".

```kotlin
val transform = InstructionReplaceTransform(name("hello"), name("world"))

adapter.rootFragment.transformChildren(transform, null)

adapter.closePatchBatch()
```

The `closePatchBatch` call instructs the adapter to close the patch batch and update the UI
(in case of [ui adapters](def://)).