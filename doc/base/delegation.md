# Delegation

When an adaptive function has the `@Delegated` annotation, the fragment is built by calling the `build`
method of the adapter, which in turn uses the `fragmentImplRegistry` to get an instance of the fragment.

```kotlin
import hu.simplexion.adaptive.base.Delegated

@Delegated
fun Adaptive.text(text : String) {
    manualImplementation(text)
}
```

The main use case of `@Delagated` is to have different implementations by platform. For example `text` have to
do very different things in browser than on Android.

The code above results in a `genBuild` like this:

```kotlin
override fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

    val fragment = when (declarationIndex) {
        0 -> adapter.build("text", this, 0)
        else -> invalidIndex(declarationIndex) // throws exception
    }

    fragment.create()

    return fragment 
}
```