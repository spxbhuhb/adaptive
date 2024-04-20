Adaptive is a UI framework for Kotlin, inspired by [Svelte](https://svelte.dev).

The concept is fundamentally different from Compose and React:

* there is no remember and there are no hooks
* state update is automatic for most use cases, no callbacks
* most of the code runs only once
* the components are stored in a tree (similar to DOM)
* updates are applied only where it is strictly necessary
* the state of the components is 100% visible from user code

```kotlin
fun Adaptive.hello() =
    div(paddig_left_10) {
        text { "Hello World" }
    }
```

Adaptive is platform antagonistic. As of know we work mostly on the browser implementation, but there is no reason
why there couldn't be one for whatever platform. Actually, the test implementation is pure Kotlin, it can be run on
any platforms.