# Instructions

A fragment may have so-called instructions. The best example is styling of UI fragments, but
instructions are not limited to styles.

Here, all the parameters of `row` are instructions. This is a mix of built-in and locally
defined instructions.

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    row(2.gridCol, greenGradient, borderRadius8, AlignItems.Center, JustifyContent.Center) {
        text("Hello World!", white)
    }
}
```

## Inner instructions

>
> [!WARNING]
>
> I really, really, really want this feature, but I haven't yet had the time to code it yet.
> There should be (and will be) rules about where and how one can use inner instructions.
>

You can put the instructions inside builder blocks, at the beginning, before any rendering instructions.
This results in much more readable code:

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    grid {
        rowTemplate(260.dp, 1.fr, 100.dp, 100.dp)
        colTemplate(1.fr)
        
        text("Hello World!", white)
    }
}
```

## Instructions used more than once

You can store instructions if you use them at different places:

```kotlin
val greenGradient = BackgroundGradient(90, lightGreen, mediumGreen)
val borderRadius8 = BorderRadius(8)
```

## Instruction sets

You can organize your instructions into arrays to make instruction sets:

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

## Non-UI instructions

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

## Defining your own

> [!NOTE]
>
> It is good practice to make all instruction classes immutable. This is not enforced now
> but it might be in the future.
>

Simply create a class or an object that implements `AdaptiveInstruction`:

```kotlin
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction

class SomeInstruction(val someString: String)

object SomeOtherInstruction : AdaptiveInstruction
```

## Finding fragments with a given instruction

Use on of:

- `firstWith`
- `firstOrNullWith`
- `singleWith`
- `filterWith`

```kotlin
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction

object A : AdaptiveInstruction
object B : AdaptiveInstruction

@Adaptive
fun someFun() {
    row {
        onClick { 
            println(adapter().firstWith<A>())
            println(adapter().firstOrNullWith<A>())
            println(adapter().singleWith<A>())
            println(adapter().filterWith<A>())
        }
        
        text("a", A)
        text("a", B)
    }
}
```

## Use in manually implemented fragments

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