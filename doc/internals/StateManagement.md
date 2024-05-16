# State Management

When a value of a variable changes, the one who changed the value has to:

- update the dirty mask
- call patch of the fragment

## Patch

Patch typically:

- calls external patch of all child components
- calls patch of all child components
- clears the dirty mask

Patch of bridge components may update the underlying UI as well.

## External Patch

External patch functions update the external state variables of a component.

They are part of the parent component because they are call-site dependent.
Two different call sites may need two different external patch functions.
In the example below patching the two T1 components requires two different computations.

```kotlin
@Adaptive
fun test(v1: Int) {
  T1(i + 1)
  T1(i + 2)
}
```

The compiler generates a function in the parent component for each call site called `adaptiveExternalPatchX` 
where `X` is the start offset of the original function call the external patch belongs to.

```kotlin
fun adaptiveExternalPatch543(fragment: AdaptiveT1) {
    it as AdaptiveT1

    if (adaptiveDirty0 and 1L) != 0L) {
        fragment.i = this.v1
        fragment.adaptiveInvalidate0(mask = 1L)
    }
}
```