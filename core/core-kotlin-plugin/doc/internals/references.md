# References

Allowed return types (all three are translated to `AdaptiveFragment`):

- `Unit`
- `AdaptiveFragment`
- `Any`

Direct fragment reference passing:

```kotlin
@Adaptive
fun foo1(p : @Adaptive () -> Any) {
    p()
}
```

Indirect fragment reference passing:

```kotlin
class C(
   val p : @Adaptive () -> Any
)

@Adaptive
fun foo2(c : C) {
    c.p()
}
```

## Direct fragment reference passing

```kotlin
@Adaptive
fun foo1(p : @Adaptive () -> Any) {
    p()
}
```

- The state variable of `p` contains a `BoundFragmentFactory` instance.

```kotlin
@Adaptive
fun caller() {
    foo1 {
        // ...
    }
}
```

- `caller.build` passes the `BoundFragmentFactory` instance to `AdaptiveAnonymous` constructor.
- `caller.patchDescendant` sets the `BoundFragmentFactory` instance for the `foo1.p` state variable.
  - when the parameter is not an inline function but a reference, it can change
- `ArmFragmentFactoryArgumentBuilder` 
  - Creates the `BoundFragmentFactory` instance.
  - Uses `transformCreateStateAccess` to get the function reference for `BoundFragmentFactory`.

## Indirect fragment reference passing

```kotlin
class C(
   val p : @Adaptive () -> Any
)

@Adaptive
fun foo2(c : C) {
    c.p()
}
```

- In this case we do not have the `BoundFragmentFactory` instance as the state variable is an instance of `C`.

>> TODO --- finish planning and implementation for indirect fragment references
