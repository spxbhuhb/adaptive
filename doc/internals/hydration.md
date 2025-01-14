# Hydration

The hydration subsystem turns fragment designs into actual, working fragments:

```kotlin
@Adaptive
fun someFun() {
    hydrated(Designs.something, "param1, "param2", 12, 23)
}
```

* Designs are handled by the standard resource subsystem.
* `Designs.something` is an instance of `DesignResource`.
* Each design has its own resource file.
* The resource file contains a serialized `AfmFragment` class (JSON or Protobuf).
* Design resources can be created Grove or with any other tools.
* Hydrated fragments have their own state just as any other fragments.
* The arguments of `hydrated` are the external state variables of the fragment.

## Abstract Fragment Model

The AFM stores the following data:

* state definition
  * external state variables
  * design-internal state variables
* internal patching
  * how to patch design-internal state variables
* descendant fragments
  * how to build the descendant
  * how to patch the descendant

"design-internal" state variables are state variables that are internal to the design. 
They are not actual internal state variables for technical reasons, but from the design
point of view they can be treated as such.

## HydratedFragment

`HydratedFragment`:

* handles the fragment design and acts as the designed fragment
* contains only external state variables in the state:
  * the design
  * the other arguments of the `hydrated` call
* delegates design-internal state variables to an anonymous fragment

```kotlin
hydrated(Designs.something, externalStateVariable1, externalStateVariable2)
```

`hydrated` calls need special processing because the `externalState` parameter is
`vararg`. This would result in an array in the state which we do not want as it
ruins the dependency based patching optimisation.

Therefore, the compiler plugin has to convert the vararg arguments of `hydrated` as
if they would be passed one-by-one.

All state variable accesses in `HydratedFragment`:

* has to check if the state variable is external or design-internal
* external: get from `HydratedFragment`
* design-internal: get from the anonymous fragment