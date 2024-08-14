# What is Adaptive

## TL;DR

**Take this with a grain of salt. Also, please read [Motivation](./impressum.md#motivation) and [Status](./status.md).**

```kotlin
object Adaptive {
    
    override val dependencyHell
        get() = throw UnsupportedOperationException()
  
    val svelte = Adaptive.ui
    val server = Adaptive.backend

    override val compose = Adaptive.ui
    override val react = Adaptive.ui
    
    override val serialization = Adaptive.wireformat
    
    override val rpc = Adaptive.services
    override val rest = Adaptive.services

    override val resources = Adaptive.resources
    override val settings = Adaptive.settings
}
```

## A bit longer

Adaptive is a software suite for building full-stack Kotlin Multiplatform applications:

Client platforms:

* browser (JS)
* Android
* iOS

Backend/Server platforms:

* all platforms supported by Kotlin Multiplatform
  * some modules may be platform dependent, but
  * the core is pure Kotlin

Tools:

* Project Wizard
* Dev Server
* UI Designer

## Adaptive in action

Web site: [https://adaptive.fun](https://adaptive.fun)

## Let's write a small application

### Step 1. - Define an API

Source set: **commonMain**

```kotlin
@ServiceApi
interface CounterApi {
    
    suspend fun incrementAndGet() : Int

}
```

### Step 2. - Implement the service

Source set: **jvmMain**

```kotlin
class CounterService : CounterApi, ServiceImpl<CounterService> {

  companion object {
    val counter = AtomicInteger(0)
  }

  override suspend fun incrementAndGet(): Int {
    return counter.incrementAndGet()
  }

}
```

### Step 3. - Write the server main

Source set: **jvmMain**

```kotlin
fun main() {

  backend(wait = true) {

    settings {
      propertyFile(optional = false) { "./etc/sandbox.properties" }
    }

    service { CounterService() }

    worker { KtorWorker() }

  }

}
```

### Step 4. - Write the common UI main

Source set: **commonMain**

```kotlin
val black = color(0x0u)
val white = color(0xffffffu)
val counterService = getService<CounterApi>()

@Adaptive
fun commonMain() {
  var counter = counterService.get()

  row {
    leftToRightGradient(black, white)
    padding { 16.dp }
    onClick { counter = counterService.incrementAnGet() }

    text("Counter: $counter") .. bold
  }
}
```

### Step 5. - Call common UI main from platform main

Source set: **jsMain**

Note: this is the only platform-dependent part of the UI.

```kotlin
fun main() {
  withWebSocketTransport()

  browser {
    commonMain()
  }
}
```

### Summary

There are quite a few points in the example above that worth mentioning.

**functions marked with @Adaptive are fragments**

Fragments are the building blocks in adaptive. The system creates a so-called fragment
tree from these fragments. The fragment tree contains the UI or the backend structure.

**fragments are reactive by default**

If you check the common UI main you'll see that when the user clicks on the row
the counter increments and then the text is updated automatically on the UI.

This is how Adaptive fragments work in general. When the data changes, variables
are update accordingly.

**server and client are both reactive**

The technological foundation is the same for client and server. There is no real difference
between the two, you only use different fragments.

This also means that Adaptive servers can react/reconfigure themselves on configuration
change, just as any reactive UI does it.

## Main concept

Adaptive differs from existing libraries in how it approaches reactivity. Originally it has been inspired
by [Svelte](https://svelte.io), but the idea has grown and turned into something much more interesting.

### Fragments

The building blocks of an Adaptive application are fragments.

Under the hood, the library:

- converts `@Adaptive` functions into stateful classes
- uses these classes to build a tree from these classes

All of these classes are able to handle state changes and update themselves
or their descendants accordingly.

So, if you change something at the top, the change is propagated down to
all fragments which depends on that specific piece of data.

### Instructions

Instructions may be added to fragments to modify them in many ways. `backgroundColor`, `onClick`, and `black`
are instructions in the code example below.

```kotlin
import `fun`.adaptive.foundation.rangeTo

@Adaptive
fun helloWorld() {
    row {
        backgroundColor(cyan)
        onClick { println("You clicked on me!") }

      text("Click on me!") .. black .. bold
    }
}
```

**instructions are NOT specific to UI**

While most styling are made with instructions UI is just a subset of meaningful instructions.

For example, there are instructions such as `name` which sets the name of a fragment, or `trace` which
switches on tracing.

These are general instructions which can be used in any context and are very-very powerful.

**instructions are part of the state**

The interesting thing about instructions is that they are added to the state of the fragment.
This has many consequences and leads us to an impressive feature list.

**well-placed instructions results in readable code**

Instead of parameters Adaptive mostly uses instructions to change fragment behaviour. While you can
pass the instructions as a parameter as well, that sometimes results in cluttered, very hard to read
code.

With the help of [inner](foundation/instructions.md#inner-instructions) and [outer](foundation/instructions.md#outer-instructions)
instructions code becomes very easy to read (and write).

## Features

The fragment/instruction approach provides a quite interesting toolset:

**the code**

- intuitive to write
- very easy to read

**the fragment tree**

- inspection and modification of the fragment tree during runtime
- replace fragment implementations globally
  - for example, you can say: whenever the `text` function called, use the `MyText` class instead of `CommonText`
- serialize/deserialize the fragment tree automatically
- can be easily used to build a UI editor

**instructions**

- easily extend functionality of existing implementations
- fragment lookup and change:
  - "get me all the red rectangles"
  - "hide all the input fields"

**develop without simulators**

- write and test almost all UI code for mobile using a web browser
- navigation, resources, layouts all can be transparent

**testing**

- using a test adapter you can
  - run headless tests for the UI
  - emulate UI actions and check the results
  - save the exact trace of everything that happens during the tests

Honestly, that list can go on. While all these functions are of course somewhat possible to implement with
other frameworks, the difference is that Adaptive was designed for this.

Most of the features above are already in the library, working.

## Goals

The ultimate goal of Adaptive is to give the end users tools to interact with the UI during runtime through an AI.

For example: "please change the color of the middle line to blue"

With Adaptive this is "easy" to implement.

Intermediate goals:

**UI designer**

* like Figma, Adobe XD etc.
* this is an easy task as the whole concept is geared towards this
* the designer can simply use the fragments themselves

**query and execute**

* query (and maybe an index) functionality to get a list of fragments based on criteria
* execute to change a list of fragments according to instructions

**AI integration**

* voice-to-text for interpreting user requests (Whisper.cpp or build-in)
* text-to-json to turn the requests into machine-readable form (Lamma.cpp maybe)
* json-to-query and json-to-operation to make actual queries and operations to execute
* interaction framework
  * microphone (on the client)
  * edge AI (voice-to-text)
  * query & operation AI (text-to-json)
  * server (send operations to execute to the client)