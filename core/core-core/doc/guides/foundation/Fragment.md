# What is a fragment

Fragments are the fundamental runtime units in Adaptive. Any typical Adaptive application builds
one or more trees of these fragments to manage application state and rendering (in the case of UI applications).

There are more ways to define a fragment:

- write a function with the `@Adaptive` annotation,
- write a manual implementation class extending `AdaptiveFragment`,
- write a server-side implementation and use one of the basic server fragments (service, worker),
- use no-code data to define the fragment.

## State and Reactivity

Each fragment has a *state*, that contains *state variables* (the data of the fragment).

State variables within a fragment can be:

- **external** – received as arguments
- **internal** – declared within the fragment

When the value of a state variable changes, the fragment is *patched*, letting the fragment
react to the change.

Adaptive keeps track of dependencies between fragments and between the state variables of
a fragment. When a value changes, it automatically updates all dependent fragments and variables.
In the case of UI fragments, this typically means updating the UI.

This patching mechanism serves as the basis of reactivity in Adaptive.

## Boundary: State vs. Rendering

Conceptually, fragments have two parts:

- **State Definition**: Defines the internal state and patching mechanisms of the fragment.
- **Rendering**: Defines how the state is rendered into an output.

These are separated by the *boundary*.

Adaptive automatically finds the boundary by finding the first call to another Adaptive function.

```kotlin
@Adaptive
fun clock() {
    val time = poll(1.seconds) { Clock.System.now() } ?: Clock.System.now()
    // ---- boundary ----
    text("Time: $time")
}
```

Rules:

- No variables/functions may be declared below the boundary (except in callback functions).
- Rendering must be stateless, except for event handlers.

## Fragment Parameters

Parameters to a fragment are external state variables. They are read-only from inside the fragment but 
can be updated externally, triggering a patching.

```kotlin
@Adaptive
fun timeWithLabel(label: String) {
    val time = poll(1.seconds) { Clock.System.now() } ?: Clock.System.now()
    text("$label: $time")
}
```

## Fragment Composition

### Composition

Fragments can contain other fragments by calling the function that defines the other fragment.
This allows complex hierarchies where the parent state propagates changes to children automatically.

```kotlin
@Adaptive
fun parent() {
    val time = poll(1.seconds) { Clock.System.now() } ?: Clock.System.now()
    child(time, 1)
    child(time, 2)
}
```

### Conditions

Standard Kotlin `if` and `when` is supported:

```kotlin
@Adaptive
fun oddHour() {
    val time = poll(1.seconds) { now() } ?: now()
    
    text("$time")
    
    if (time.toLocalDateTime(TimeZone.currentSystemDefault()).hour % 2 != 0) {
        text("what an odd hour is this")
    }
}
```

### Loops

Basic Kotlin `for` is supported:

```kotlin
@Adaptive
fun list() {
    for (i in 0..3) {
        text { "list item: $i" }
    }
}
```

Note: `forEach` is **NOT** supported.

### Higher Order Fragments

Higher order fragments can be defined by declaring a parameter which 
is a function and is annotated with `@Adaptive`.

```kotlin
import `fun`.adaptive.foundation.Adaptive

@Adaptive
fun higherOrder(@Adaptive sub: (time: Instant) -> Unit) {
    
    val time = poll(1.seconds) { now() } ?: now()

    sub(time)
}

@Adaptive
fun callHigherOrder() {
    higherOrder {
        text(it)
    }
}
```

## Accessing the fragment

These functions help access the fragment from the [original function](def://):

- [adapter](function://fun.adaptive.foundation.functions) - gets the adapter of the fragment
- [fragment](function://fun.adaptive.foundation.functions) - gets the fragment itself
- [instructions](function://fun.adaptive.foundation.functions) - gets the instructions of the fragment

All these functions return with the data of the [declaring fragment](def://).
 
## State Dependency and Independence

By default, all state variables are reactive and are updated when a variable
they depend on changes:

```kotlin
@Adaptive
fun t1() {
    val time = poll(1.seconds) { now() } ?: now()
    t2(time)
}

@Adaptive
fun t2(time : Instant) {
    val displayTime = time.toLocalDateTime(TimeZone.currentSystemDefault())
    text(displayTime)
}
```

In the example above, `displayTime` changes whenever `time` changes. Adaptive takes
care of change distribution automatically.

To stop automatic updates, use the `@Independent` annotation. When this annotation is added,
the value is calculated only once: when the fragment is initialized the first time.

In this example, `displayTime` never changes, even if time changes.

```kotlin
@Adaptive
fun t2(time : Instant) {
    
    @Independent
    val displayTime = time.toLocalDateTime(TimeZone.currentSystemDefault())
    
    text(displayTime)
}
```

> [!NOTE]
>
> Independent **DOES NOT** mean static and/or immutable. Independent state
> variables can change, just not because of the other variables used to calculate them.
>
> You can change them explicitly (if they are declared as `var`) or they can change
> automatically if they have a [Producer](guide://).
>

## Lifecycle

Fragments follow a defined lifecycle:

1. a new, uninitialized instance is created (state contains nulls)
2. call `create`
   1. call `patch`
      1. call `declaringFragment.genPatchDescendant` to set the external state variables
      2. call `genPatchInternal` to set internal state variables
   2. call `genBuild` to create child fragments
3. call `mount`
   1. call `parent.addActual` to put the fragment into the actual UI tree (if there is one)
   2. call `child.mount` to put the child fragments into the actual UI
4. ... life goes on ...
5. call `unmount`
   1. call `child.unmount` to remove the child fragments from the actual UI
   2. call `parent.removeActual` to remove the fragment from the actual UI tree (if there is one)
6. call `dispose`
   1. call `child.dispose` to dispose all descendant fragments
   2. remove all producers by calling `removeProducer` on it
   3. remove all bindings by calling `removeBinding` on it

## Background Tasks

Fragments themselves do not manage background tasks, patching is synchronous.

That said, many functions do start something in the background, for example:

- `poll` - launches a coroutine to poll the resource in the given intervals
- `worker` - launches a coroutine that calls the `run` function of the worker

These tasks are stopped when the fragment is disposed, see [lifecycle](#lifecycle).

To dispose a whole adaptive tree (for example when an Android main activity stops, but the process does not exit)
call `unmount` and then `dispose` on `AdaptiveAdapter.rootFragment`.

Alternatively you can call `AdaptiveFragment.throwAway`.

The adapter is the return value of the entry call:

```kotlin
val adapter = android {
    /* ... */
}

fun dispose() {
    adapter.rootFragment.throwAway()
}
```

## Function References

> [!WARNING]
>
> Function reference support is unstable and may not work in all cases.
>
> While function references work in general, this issue blocks some
> use cases such as passing the reference in a class:
>
> [KT-75416](https://youtrack.jetbrains.com/issue/KT-75416)
>

To use function references mark the property or parameter with `@Adaptive` to allow
the compiler plugin to recognize it as an Adaptive function reference.

```kotlin
class Config(
   @Adaptive
   val fragmentFun : () -> Unit
)

@Adaptive
fun someFun(@Adaptive : f : () -> Unit) {
    f()
}

@Adaptive
fun useRef(config : Config) {
    someFun(config.fragmentFun)
}
```

Probably works with:

- class properties - `/core/core-kotlin-plugin/testData/box/foundation/reference/classPropertyWithParam.kt`
- global properties - `/core/core-kotlin-plugin/testData/box/foundation/reference/globalPropertyWithParam.kt`
- direct - `/core/core-kotlin-plugin/testData/box/foundation/reference/hardWithParam.kt`
- instructions - `/core/core-kotlin-plugin/testData/box/foundation/reference/classPropertyWithReturn.kt`

Surely **DOES NOT** work for:

- nullable properties

```kotlin
@Adaptive
val p : () -> Unit = {  }
```

# Notes

- Avoid using variable declarations in the rendering section.
- Only supported function references should be used.
- Fragment execution is synchronous and deterministic in behavior.

# See Also

- [What is an instruction](guide://)
- [Producer](guide://)

# Conclusion

Fragments provide the building blocks for reactive, declarative, and platform-independent UIs 
in Adaptive Kotlin. Understanding their lifecycle, state management, and composition is key
to using the framework effectively.
