# Expect Fragments

When an adaptive function has the `@AdaptiveExpect` annotation, the fragment is built by calling the `actualize`
method of the adapter, which in turn uses the `fragmentImplRegistry` to get an instance of the fragment.

```kotlin
import hu.simplexion.adaptive.base.AdaptiveExpect

@AdaptiveExpect
fun text(text : String) {
    manualImplementation(text)
}
```

> **IMPORTANT**
> In contrast with Kotlin expect/actual a missing implementation does not raise a compilation error for @AdaptiveExpect.
> You will get a runtime exception if there is no implementation.

The main use case of `@AdaptiveExpect` is to have different implementations by adapter.

The code above results in a `genBuild` like this:

```kotlin
override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

    val fragment = when (declarationIndex) {
        0 -> adapter.actiualize("hu.simplexion.adaptive.ui.basic.AdaptiveText", this, 0)
        else -> invalidIndex(declarationIndex) // throws exception
    }

    fragment.create()

    return fragment 
}
```