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

| Platform    | Function Name       | Adapter Class            | Bridge              | Comments                                         |
|-------------|---------------------|--------------------------|---------------------|--------------------------------------------------|
| commonMain  | `server`            | `AdaptiveServerAdapter`  | `Any`               | You can use the server adapter on all platforms. |
| commonMain  | `test`              | `AdaptiveTestAdapter`    | `TestNode`          |                                                  |      
| jsMain      | `browser`           | `AdaptiveDOMNodeAdapter` | `org.w3c.Node`      | Standard browser stuff, HTML.                    |
| androidMain | `android`           | `AdaptiveViewAdapter`    | `android.view.View` |                                                  |
| iosMain     | not implemented yet | not implemented yet      | `UiView`            |                                                  |

Points of interest:

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
    text { "Hello World!" }
}
```

## Basics: State

Each fragment has a *state*. When this state changes the component automatically
updates the UI to reflect the change.

Defined variables are part of the component state, and they are *reactive by default*.
We call these *internal state variables*.

```kotlin
fun Adaptive.counter() {
    var counter = 0
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

The state of this component consists of the `counter` variable. Whenever you
click on the button, the `counter` is incremented. The component realizes that
the `counter` has been changed and updates the UI.

## Basics: Parameters

You can add parameters to the component. Parameters are parts of the
component state. We call these *external state variables*.

You cannot change the external state variables from the inside of
the component. However, it may happen that the parameter changes
on the outside. In that case the component updates the UI automatically.

```kotlin
fun Adaptive.counter(label: String) {
    var counter = 0
    filledButton { "$label: $counter" } onClick { counter++ }
}
```

## Basics: Boundary

Adaptive components have two main areas: *state initialization* and *rendering*.
These are separated by the *boundary*.

Above the boundary, you initialize the component state. This is a one-time
operation, executed when the component is initialized.

Below the boundary, you define how to render the component. This part
is executed whenever the state changes.

**Very important** you cannot define variables, functions etc. in the
*rendering* (except in event handlers, see later). This is a design decision we've
made to avoid confusion. The compiler will report an error if you try to do so.

Adaptive automatically finds the *boundary*: the first call to another Adaptive component
function marks the *boundary*.

```kotlin
fun Adaptive.counter() {
    var counter = 0
    // ---- boundary ----
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

## Basics: Support Functions

*Support functions* are functions called when something happens: user input,
completion of a launched co-routine etc.

Support functions may change state variables and these changes result in a UI update.

You can define support functions as local functions or as lambdas. Adaptive recognizes
when a block changes a state variable and automatically updates the UI.

The handlers in this example are equivalent. Note that whichever button you
click, labels of all show the new counter value.

```kotlin
fun Adaptive.counter() {
    var counter = 0

    fun increment() {
        counter++
    }

    filledButton { "Click count: $counter" } onClick ::increment
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

## Basics: Higher Order Components

Components may contain other components, letting you build complex UI
structures. When `counter` of the parent component changes the child
automatically updates the child component.

```kotlin
fun Adaptive.child(counter: Int) {
    text { "Click count: $counter" }
}

fun Adaptive.parent() {
    var counter = 0
    child(counter) onClick { counter++ }
}
```

## Basics: Conditions

You can use the standard Kotlin `if` and `when` to decide what to display.

```kotlin
fun Adaptive.counter() {
    var count = 0
    filledButton { "click count: $count" } onClick { count++ }
    if (count < 3) {
        text { "(click count is less than 3)" }
    }
}
```

```kotlin
fun Adaptive.counter() {
    var count = 0
    
    when (count) {
        1 -> text { "click count: 1" }
        2 -> text { "click count: 2" }
        else -> text { "click count > 2" }
    }
    
    filledButton { "click to increment" } onClick { count++ }
}
```

## Basics: For Loops

```kotlin
fun Adaptive.list() {
    for (i in 0..3) {
        text { "list item: $i" }
    }
}
```

## Lifecycle

All adaptive components follow the same lifecycle pattern:

1. a new, uninitialized instance is created (state contains nulls)
2. call to `create`
3. call to `mount`
4. call to `unmount`
5. call to `dispose`

## Background Tasks

Adaptive never starts background tasks by itself, everything is a result of your code.

That said, many functions do start something in the background, for example:

- `poll` - launches a coroutine to poll the resource in the given intervals
- `worker` - launches a coroutine that calls the `run` function of the worker
