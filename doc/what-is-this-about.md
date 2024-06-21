# What is Adaptive

Adaptive is a Kotlin Multiplatform library for full-stack (server + Browser/JS, Android, iOS) application development.

## Main concept

Adaptive differs from existing libraries in how it approaches reactivity. Originally it has been inspired
by [Svelte](https://svelte.io), but the idea has grown and turned into something much more interesting.

The soul of Adaptive is the so-called foundation transformation and the so-called instructions.

The transformation turns the functions marked with the `@Adaptive` annotation into stateful classes
that are **reactive** by default. During runtime these classes are used to build a tree of fragments
that reflect the current state of the application.

Something like this:

![Adaptive Transform](adaptive-transform.png)

Instructions may be added to fragments to modify them in many ways. `backgroundColor`, `onClick`, and `black`
are instructions in the code example below.

```kotlin
@Adaptive
fun helloWorld() {
  row {
    backgroundColor(cyan)
    onClick {
      println("hello world!")

      text("Click on me!", black)
    }
  }
```

The interesting thing about instructions is that they are added to the state of the fragment. And this leads us to
the features Adaptive provides.

## Unique Features

This approach provides a quite unique toolset:

**the code**

- intuitive to write
- very easy to read

**the fragment tree**

- inspection and modification of the fragment tree during runtime
- replace fragment implementations globally
  - for example, you can say: whenever the `text` function called, use the `MyText` class instead of `CommonText`
- serialize/deserialize tree automatically
- can be easily used to build a UI editor

**instructions**

- easily extend functionality of existing implementations
- fragment lookup and change:
  - "get me all the red rectangles"
  - "hide all the input fields"

**develop without simulators**

- write and test **all** UI code for mobile using a web browser
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