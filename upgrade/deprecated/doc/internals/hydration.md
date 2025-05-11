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

## Linear Fragment Model (LFM)

The Linear Fragment Model is a data model Adaptive uses to store fragment designs.
This model is in-line with the model the compiler plugin uses when it converts
Kotlin code into adaptive fragment classes.

The LFM of a fragment consists of:

* state definition
  * external state variables
    * name
    * signature
    * default value expression
  * internal state variables
    * name
    * signature
    * dependency mask
    * calculation expression
* descendant list
  * for each descendant:
    * key to the fragment build function
    * state variable mapping
      * dependency mask (source state variable indices)
      * target state variable index
      * mapping function

The model is called "linear" because all descendants are stored in a single linear list.

In general, manually written fragments are not linear:

```kotlin
@Adaptive
fun someFun(a : Int, b : Int) {
    val c = 12
    
    row {
        column {
            someOtherFun(a,b)
            someOtherFun(c)
        }
    }
}
```

This function describes a fragment tree which is turned into a linear model as
shown below. The brackets of descendants shows the state passed to the given
descendant.

```text
external: [a, b]
internal: [c]
descendants: [
    "aui:row" [1] -- here 1 is the index of the first "anonymous"
    "anonymous" [2] -- 2 is the index of "aui:column"
    "aui:column [3] -- 3 is the index of the second "anonymous"
    "anonymous" [4] -- 4 is the index of the sequence
    "aui:sequence" [[5, 6]] -- indices of someOtherFunCalls
    "someOtherFun" [a, b]
    "someOtherFun" [c]
]
```

LFM stores the structural information in the indices passed to the descendants.

For example "aui:row" knows that he has to create the fragment at index `1` to
build the content of the row.

Fragments compiled from Kotlin code handle the internal state differently than
the ones built from an LFM. The reason is that at the time of compilation
there is no way to figure out how many internal state variables a hydrated
fragment has.

So, hydrated fragments have a single `AnonymousFragment` child which actually
stores the internal state of the hydrated fragment.

## Plugin processing

```kotlin
hydrated(Designs.something, externalStateVariable1, externalStateVariable2)
```

`hydrated` calls need special processing because the `externalState` parameter is
`vararg`. This would result in an array in the state which we do not want as it
ruins the dependency based patching optimisation.

Therefore, the compiler plugin has to convert the vararg arguments of `hydrated` as
if they would be passed one-by-one.

`IrFunction2ArmClass` checks if a call has the `AdaptiveHydrated` annotation
and performs the transformation from vararg to individual state variable arguments
for such calls.

When actualizing the fragment `ArmCallBuilder` passes the number of the external
state variables to the fragment. This makes it possible to size the hydrated
fragment state correctly.

## GroveHydrated

`GroveHydrated`:

* handles the fragment design and acts as the designed fragment
* contains only external state variables in the state:
  * the LFM
  * the other arguments of the `hydrated` call
* delegates internal state variables to an anonymous fragment

All state variable accesses in `GroveHydrated`:

* has to check if the state variable is external or internal
* external: get from `GroveHydrated`
* design-internal: get from the anonymous fragment