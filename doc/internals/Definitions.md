## Definitions

### General

compiler plugin
: The Z2 compiler plugin which transforms *original functions* into *component classes*.

entry point
: A call to the `adaptive` function. The lambda passed to this call is a *root original function*.
 
```kotlin
 fun main() {
     // the call below is the entry point
     adaptive { // this lambda is the root original function
        Text("Hello World")
    }
}
```

### Functions

original function
: A Kotlin function with the receiver `ADAPTIVE`. These functions are transformed into *components classes* by the *compiler plugin*.

Types of original functions:

- *named original function*
- *root original function*
- *higher order original function*
- *anonymous original function*

named original function
: An *original function* that is not a lambda.

Name of the *component class* is `Adaptive<function-name>` where `<function-name>` is the capitalized name of the
*named original function*.

```kotlin
fun ADAPTIVE.a(p1 : Int) { // transformed into class AdaptiveA
    // ...
}
```

root original function
: An *original function* that is passed to the `adaptive` function at an *entry point*. The *compiler plugin* transforms
the *root original function* into a *component class*.

Name of the *component class* is `AdaptiveRootFFFNNN` where `FFF` is the name of the source file (without the '.kt' extension) and
`NNN` is the offset (number of characters in the source file) of lambda function. For example: `AdaptiveRootHello342`.

```kotlin
fun main() {
    adaptive { 
        // this lambda is the root original function
        // transformed into AdaptiveRootMain123
    }
}
```

#### higher order original function

A *named original function* that has a function type argument with `ADAPTIVE` receiver.

```kotlin
fun ADAPTIVE.higherOrderFun(p1 : Int, builder : ADAPTIVE.(sp1 : Int) -> Unit) { // transformed into class AdaptiveHigherOrderFun
    builder(p1 * 2)
}
```

#### anonymous original function

A Kotlin lambda, passed as a parameter to a *higher order original function*. May have only *rendering statements*, may
not have *initialization*.

The compiler plugin **does not** transform *anonymous original functions* into classes. Instead, it uses the `AdaptiveAnonymous`
class and all necessary functions will be put into the *component class* of the *named original function* in which
the *anonymous original function* is declared.

```kotlin
fun ADAPTIVE.higherOrderCall() { // transformed into class AdaptiveHigherOrderCall
    higherOrderFun(1) { // this is a call, so no class created here
        // this lambda is the anonymous function, no class created, uses AdaptiveAnonymous
    }
}
```

### Components

#### named component

A component created from a named original function by the plugin.

#### anonymous component

A component created from an anonymous function that is declared in an original function by the plugin.

### Classes

#### component class

A *component class*:

- extends `AdaptiveFragment` 
- defines the *state variables* of the *component state*
- may contain *builder* and *external patch* functions to build and patch the *adaptive tree*

The compiler plugin generates *component classes* from:

- *named original functions*
- *root original functions*

#### root component class

A *component class* that belongs to a *root original function*.

#### anonymous component class

A *component class* that belongs to an *anonymous original function*. As of now this is always `AdaptiveAnonymous`.
This class does not contain any builder or external patch functions.

#### structural class

One of `AdaptiveSequence`, `AdaptiveSelect`, `AdaptiveLoop` classes. Structural classes are stateless, they
use the *closure* they are defined in.

#### bride component class

A *component class* that manages the underlying UI.

### Instances

#### component instance

An actual instance of a *component class*.

#### root component instance

An actual instance of a *root component class*.

#### anonymous component instance

An actual instance of an *anonymous component class*.

#### structural instance

An actual instance of a *structural class*.

#### fragment instance

An actual instance which is a *component instance* or a *structural instance*.

### State

#### component state

A set of *state variables*.

#### state variable

- A part of the *component state*. 
- Each state variable has a property in the *component class*.
- Two ways to declare:
  - an argument of an *original function* (*external state variable*)
  - a top-level variable in a *named original function* (*internal state variable*)

```kotlin
// component state:
//   p1 : Int - external state variable
//   p2 : Int - internal state variable
fun ADAPTIVE.a(p1 : Int) {
    var p2 = 1
}
```

#### external state variable

An *external state variable* is a *state variable* that is derived from an argument of
the *original function*.

#### internal state variable

An *internal state variable* is a *state variable* that is derived from a variable defined
in the body of the *original function*.

### Scopes

#### declaration scope

A *component state*.

#### anonymous scope

A state defined by an *anonymous original function*. Consists of the function parameters.

#### closure

A state defined as the union of the *declaration scope* and all *anonymous scopes* up until the given *call site*.







#### runtime

The *runtime* contains base classes and interfaces, it is part of Z2 core. It is important that
this is a "static" runtime, there is no engine running in the background to perform updates.

### call site

A point in the code where an *original function* is called. Call sites identify the exact points
of function call with the very specific arguments passed to the function at that given call.

### call site dependency mask

A bitmask that contains 1 for each *state variable* the given function call uses in any form: passed
as is, used in a calculation of an argument value, used in the body of an *anonymous function*.
This mask is used to decide if the *fragment* has to be patched or not.

### adaptive fragment tree

When the program runs, it builds a tree of *fragment* instances which is *adaptive fragment tree*.