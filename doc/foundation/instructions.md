# Instructions

A fragment may have so-called instructions. The best example is styling of UI fragments, but
instructions are not limited to styles.

Here, all the parameters of `row` are instructions. This is a mix of built-in and locally
defined instructions.

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    row(2.column, greenGradient, borderRadius8, AlignItems.Center, JustifyContent.Center) {
        text("Hello World!", white)
    }
}
```

Instructions are simple classes, you can store them if you use them at different places:

> [!NOTE]
> 
> It is good practice to make all instruction classes immutable. This is not enforced now
> but it might be in the future.
>

```kotlin
val greenGradient = BackgroundGradient(90, lightGreen, mediumGreen)
val borderRadius8 = BorderRadius(8)
```

You can organize your instructions into arrays:

```kotlin
val someStyles = arrayOf<AdaptiveInstriction>(greenGradient, borderRadius8, AlignItems.Center, JustifyContent.Center)

@Adaptive
fun someFun() {

    row(2.gridCol, *someStyles) {
        text("Hello World!", white)
    }

}
```

You can also mix array instructions with single ones:

```kotlin
val centered = arrayOf<AdaptiveInstriction>(AlignItems.Center, JustifyContent.Center)

@Adaptive
fun someFun() {

    row(2.gridCol, *centered, greenGradient, borderRadius8) {
        text("Hello World!", white)
    }

}
```

Instructions are not limited to styling nor to UI fragments. This is a server side
instruction:

```kotlin

class DelayStart(val duration: Duration) : AdaptiveInstuction

class SomeWorker : WorkerImpl<SomeWorker> {
    override suspend fun run() {
        ifHas<DelayStart> { delay(it.duration) }
    }
}

fun main() {

    server {
        worker(DelayStart(5.minutes)) { SomeWorker() }
    }

}
```

## Trace

The `Trace` instruction may be used to switch on trace locally for a fragment:

```kotlin
@Adaptive
fun someFun() {

    row(2.gridCol, *someStyles, Trace(Regex(".*"))) {
        text("Hello World!", white)
    }

}
```

Parameters of `Trace` are regular expressions to filter the trace by the point of the trace. 
For example `Trace("layout.*")` adds all trace lines with points that start with "layout".

> [!IMPORTANT]
>
> This is a regular expression, not a string. `*` does not match everything, use `.*`
>

## Manual Implementation

To let a fragment accept instructions add a `vararg` parameter called `instructions` with
the type `AdaptiveInstruction`.

To access instructions of another fragment from your manual implementation
use `AdaptiveFragment.instruction`.

The value of this property is the current instruction set from the state 
of the component, type is `Array<out AdaptiveInstruction>`.

If a given fragment does not have instructions (no parameter with the name
and type) you get an empty array.

The index of the instruction state variable is stored in the `instructionIndex`
property of the fragment.

## Future Plans

Issue: #18

Add support for inner instructions. The code below does not have the parenthesis
and comma from being an argument, it is much more readable.

This needs plugin support as these calls are in the rendering part and have
to be moved into the state-definition part.

```kotlin
grid {
    RowTemplate(260.dp, 1.fr, 100.dp, 100.dp)
    ColTemplate(1.fr)

    /* ... */
}
```