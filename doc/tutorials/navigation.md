# Navigation

Adaptive does not provide traditional navigation such as routing based on URLs or the Navigation component on Android.

Instead, we give you the tools to change the fragment tree based on selectors.

Theoretically any fragments could be replaced, but for now the general pattern is to define a `slot` and
then instruct a fragment with `Replace` or `replace` (the two are the same).

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    slot {
        first()
    }
}

@Adaptive
fun first() {
    text("First, click to change to second!", Replace { second() })
}

@Adaptive
fun second() {
    text("Second, click to change to first!", replace { first() })
}
```

`replace` walks up the fragment tree and finds the first slot. The fragment of that slot is replaced
with the ones specified in the argument function.

> [!CAUTION]
> 
> You have to use `replace` directly, this **DOES NOT** work:
> 
> ```kotlin
> val r = Replace { text("hello") }
> 
> @Adaptive
> fun someFun() {
>    text("a", r)
> }
> ```
>
> Theoretically it could work, but it's not implemented in the plugin.

## Parameters

You can also pass parameters to the replacements:

```kotlin
@Adaptive
fun someFragment(i : Int) {
    text("Click to replace with a parameter", Replace { someOtherFragment(i + 1) })
}
```

> [!CAUTION]
> 
> The values you pass to the new fragment are **detached** from the fragment that calls contains `Replace`.
> This means that the changes of `i` in the example above are **NOT** propagated to `someOtherFragment`.
> 
> This is intentional.
> 

## History

You can implement a navigation history if you use set the `historySize` parameter of the `slot` function to a positive
value:

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    slot(historySize = 10) {
        first()
    }
}

@Adaptive
fun first() {
    text("First, click to change to second!", Replace { second() })
}

@Adaptive
fun second() {
    text("Second, click to go back!", Back)
}
```

## Internals

Related internal details:

- [Tree operations](../internals/tree-operations.md)

