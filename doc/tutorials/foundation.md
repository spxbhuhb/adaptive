## Basics: Getting Started

Start adaptive by calling one of the entry functions, `browser` for example:

```kotlin
fun main() {
    browser {
        text("Hello World!")
    }
}
```

Available entry functions (bridge is used to connect Adaptive to the underlying software):

| Platform    | Function Name | Adapter Class            | Bridge                    | Comments                                         |
|-------------|---------------|--------------------------|---------------------------|--------------------------------------------------|
| commonMain  | `server`      | `AdaptiveServerAdapter`  | `Any`                     | You can use the server adapter on all platforms. |
| commonMain  | `test`        | `AdaptiveTestAdapter`    | `TestNode`                |                                                  |      
| jsMain      | `browser`     | `AdaptiveBrowserAdapter` | `org.w3c.dom.HTMLElement` | Standard browser stuff, HTML.                    |
| androidMain | `android`     | `AdaptiveAndroidAdapter` | `android.view.View`       |                                                  |
| iosMain     | `ios`         | `AdaptiveIOSAdapter`     | `UiView`                  |                                                  |

Notes:

* entry functions return with an instance of the adapter class
* the returned adapter instance contains everything built by the block passed to the entry function
* the adapter never starts background tasks for itself
* the adapter may have background tasks, if you create them some way, details in [background tasks](#background-tasks)
* to shut down everything properly call the `unmount` and `dispose` functions of the adapter, details in [lifecycle](#lifecycle)

## Basics: Fragments

The basic building blocks of an Adaptive application are fragments.

Add the `@Adaptive` annotation to tell the compiler to turn the function into a fragment.

```kotlin
@Adaptive
fun helloWorld() {
    text("Hello World!")
}
```

## Basics: State

Each fragment has a *state*. When this state changes the fragment automatically
updates the UI to reflect the change.

Defined variables are part of the fragment state, and they are *reactive by default*.
We call these *internal state variables*.

The state of the fragment below consists of the `time` internal state variable.

`poll` is a [producer](producer.md), it gets the current time each second and sets `time`
to the value. As a result, the text shown is updated to the current time each second.

```kotlin
fun now() =
    Clock.System.now()

@Adaptive
fun time() {
    val time = poll(1.seconds, now()) { now() }
    text("$time")
}
```

## Basics: Parameters

You can add parameters to a fragment. Parameters are parts of the
fragment state. We call these *external state variables*.

You cannot change the external state variables from the inside of
the fragment. However, it may happen that the parameter changes
on the outside. In that case the fragment updates the UI automatically.

```kotlin
@Adaptive
fun timeWithLabel(label: String) {
    val time = poll(1.seconds, now()) { now() }
    text("$label: $time")
}
```

## Basics: Boundary

Adaptive fragment have two main parts: *state initialization* and *rendering*.
These are separated by the *boundary*.

Above the boundary, you initialize the fragment state. This is a one-time
operation, executed when the fragment is initialized.

Below the boundary, you define how to render the fragment. This part
is executed whenever the state changes.

> [!IMPORTANT]
>
> **Very important** you cannot define variables, functions etc. in the
> *rendering* (except in event handlers, see later). This is a design decision we've
> made to avoid confusion. The compiler will report an error if you try to do so.
>

Adaptive automatically finds the *boundary*: the first call to another Adaptive
function marks the *boundary*.

```kotlin
@Adaptive
fun timeBoundary() {
    val time = poll(1.seconds, now()) { now() }
    // ---- boundary ----
    text("$label: $time")
}
```

## Basics: Sequence 

Fragments may contain other fragments, letting you build complex structures.
When `time` of the parent fragment changes the parent will tell the children
that there has been a change and in turn the child fragments will update as well.

```kotlin
@Adaptive
fun child(time: Instant, number : Int) {
    text("$time from the child $number")
}

@Adaptive
fun parent() {
    val time = poll(1.seconds, now()) { now() }
    child(time, 1)
    child(time, 2)
}
```

## Basics: Conditions

You can use the standard Kotlin `if` (`when` probably also works, I should try it out).

```kotlin
@Adaptive
fun oddHour() {
    val time = poll(1.seconds, now()) { now() }
    
    text("$time")
    
    if (time.toLocalDateTime(TimeZone.currentSystemDefault()).hour % 2 != 0) {
        text("what an odd hour is this")
    }
}
```

## Basics: For Loops

```kotlin
@Adaptive
fun list() {
    for (i in 0..3) {
        text { "list item: $i" }
    }
}
```

## Basics: Higher Order Fragments

You can define higher order fragments by declaring a parameter which is
a function and is annotated with `@Adaptive`.

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun higherOrder(@Adaptive sub: (time: Instant) -> Unit) {
    
    val time = poll(1.seconds, now()) { now() }

    sub(time)
}

@Adaptive
fun callHigherOrder() {
    higherOrder {
        text(it)
    }
}
```

For a more complex example see: [higherOrder.kt](/adaptive-kotlin-plugin/testData/box/foundation/call/higherOrder.kt).

## Lifecycle

All adaptive fragments follow the same lifecycle pattern:

1. a new, uninitialized instance is created (state contains nulls)
2. call `create`
    1. call `patch`
        1. call `declaringFragment.patchDescendant` to set the external state variables
        2. call `genPatchInternal` to set internal state variables
    2. call `genBuild` to create child fragments
3. call `mount`
    1. call `parent.addActual` to put the fragment into the [actual UI](../internals/actual%20UI.md) tree (if there is one)
    2. call `child.mount` to put the child fragments into the actual UI
4. ... life goes on ...
5. call `unmount`
    1. call `child.unmount` to remove the child fragments from the actual UI
    2. call `parent.removeActual` to remove the fragment from the [actual UI](../internals/actual%20UI.md) tree (if there is one)
6. call `dispose`
    1. call `child.dispose` to dispose all descendant fragments 
    2. remove all producers by calling `removeProducer` on it
    3. remove all bindings by calling `removeBinding` on it 

## Background Tasks

Adaptive itself does not have any background tasks, patching is synchronous.

That said, many functions do start something in the background, for example:

- `poll` - launches a coroutine to poll the resource in the given intervals
- `worker` - launches a coroutine that calls the `run` function of the worker

These tasks are stopped when the fragment is disposed, see [lifecycle](#lifecycle).

To dispose a whole adaptive tree (for example when an Android main activity stops, but the process does not exit)
call `unmount` and then `dispose` on `AdaptiveAdapter.rootFragment`.

The adapter is the return value of the entry call:

```kotlin
val adapter = android {
    /* ... */
}

fun dispose() {
    adapter.rootFragment.unmount()
    adapter.rootFragment.dispose()
}
```