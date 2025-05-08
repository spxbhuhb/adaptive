# Internals - Producers

Patching produced values is a bit complicated because there is a difference between patching
the producer itself or patching the actual value produced.

The producer call may be nested in a chain of calls:

```kotlin
val a = periodic(1.seconds) { Clock.System.now() }.toLocalDateTime(TimeZone.getSystemDefault()).let { "${it.hour}:${it.minute}" }
```

- when any state variable the producer call depends on changes
    - replace the producer (call the producer function)
    - execute the post-processing part on the new produced value
    - set the state variable
- when any state variable the post-processing part depends on changes
    - execute the post-processing part on the latest produced value
    - set the state variable
- when the producer produces a new value
    - execute the post-processing part on the latest produced value
    - set the state variable

The logic above in `genPatchInternal`

```kotlin
if (haveToPatch(dirtyMask, producerDependencyMask)) {
    // this call:
    //   - sets the value in the producer (null if not ready)
    //   - sets the bit `index` to 1 in the dirty mask
    producer(/* ... parameters ... */ index) { /** ... producerFun ... */ }
}

if (haveToPatch(dirtyMask, (1 shl index) or postProcessingDependencyMask)) {
    // this is whatever is after the producer call
    // the producer call is replaced by `AdaptiveFragment.getProducedValue(index)`
    postProcess()
}
```

- `StateDefinitionTransform` checks if the state variable is a produced one, if so:
    - replaces the producer call with `getProducedValue`
    - calculates dependency masks for the producer and for postProcess

- `ArmInternalStateVariableBuilder`
    - generates the code for the logic above
    - replaces the binding of the producer call with a proper binding