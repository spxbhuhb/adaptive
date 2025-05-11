# Instructions

All fragments have a list of instructions (which may be empty).

The best example is styling of UI fragments, but instructions are not limited to styles.

> [!IMPORTANT]
> 
> Adding instructions to your fragments **DOES NOT** apply the UI effect on the fragment itself.
> 
> You have to pass them to an actual UI fragment such as `row` or `text` to have coloring, background, etc.
>

This code snippet shows some ways to add instructions to your fragments:

```kotlin
@Adaptive
fun someFun() {

    row(border.outline, cornerRadius8) {
    
        alignItems.center .. alignSelf.start
        padding { 16.dp }
    
        text("Hello World!") .. textSmall .. onClick { println("Hello") }
    }
    
}
```

**Ways to add instructions**

* arguments of `row` are [argument instructions](#argument-instructions)
* first two lines of `row` are [inner instructions](#inner-instructions)
* the ones following `text` are [outer instructions](#outer-instructions)

**Rules**

* The different ways are functionally equivalent.
* All fragments have **ONE** `AdaptiveInstructionGroup` which contains **ALL** instructions.
* Precedence is:
   * when ways are different:
     * outer (strongest)
     * inner
     * argument (weakest)
   * when way is the same:
     * an instruction specified later overrides ones specified earlier

**Reactivity**

* Instructions are part of the fragment state, they are **fully reactive**.
* This applies to all instruction passing ways.
* In the example below, when
  * `fixed` changes
    * `grid` `colTemplate` changes
      * the whole grid layout is updated
        * `text` will be repositioned according to the available space

```kotlin
@Adaptive
fun someFun(fixed : Boolean) {

    grid {  
        if (fixed) {
            colTemplate(200.dp) {
        } else {
            colTemplate(1.fr)
        }
        
        text("Hello World!") .. alignSelf.center
    }
}
```

## Argument instructions

Adaptive functions that support argument instructions declare an `instructions` vararg parameter.

You can simply pass your instructions in the function call.

```kotlin
@Adaptive
fun someFun(vararg instructions : AdaptiveInstruction) {
    // ...
}
```

The `instructions` parameter **cannot be accessed directly** in the function, the plugin reports an error if
you try that.

Use the `instructions()` function to get the `AdaptiveInstructionGroup` that contains
all the instructions the fragment has from all sources (argument, inner, outer).

```kotlin
@Adaptive
fun someFun() {
    if (blue in instructions()) {
        text("blue")
    } else {
        text("not blue")
    }
}
```

## Inner instructions

Inner instructions can be used in lambda functions to make code easier to read.

* In this example, `grid` has two inner instructions. 
* You can use `if`, `when` or any other expression that actually returns with an instruction.
* Instructions **MUST** follow the opening bracket, **you cannot mix rendering statements and inner instructions**.

```kotlin
@Adaptive
fun someFun(fixed : Boolean) {

    grid {
        rowTemplate(260.dp, 1.fr, 100.dp, 100.dp)
        
        if (fixed) {
            colTemplate(200.dp) {
        } else {
            colTemplate(1.fr)
        }
        
        text("Hello World!") .. alignSelf.center
    }
}
```

## Outer instructions

* Use the `..` (rangeTo) operator to add outer instructions.
* Works only if the fragment supports it (returns with `AdaptiveFragment`).
* Result in much more readable code.

```kotlin
@Adaptive
fun someFun() {
    text("Hello World!") .. white .. titleMedium .. bold
}
```

To support outer instructions:

- add return type `AdaptiveFragment`
- add return statement `fragment()`
- use `instructions()` to access the instructions
  - you can use `instructions()` deeper in the rendering
  - but it **always** returns with the instructions of the **declaring fragment** (`navButton` in this example)

```kotlin
@Adaptive
fun navButton(label: String): AdaptiveFragment {

    row {
        button .. instructions()
    
        text(label) .. color(0xffffffu) .. fontSize(15.sp) .. noSelect
    }

    return fragment()
}
```

## Instruction reuse

You can store instructions if you use them at different places:

```kotlin
val greenGradient = backgroundGradient(90, lightGreen, mediumGreen)
val cornerRadius8 = cornerRadius(8)
```

## Instruction groups

You can organize instructions into groups:

```kotlin
val someStyles = instructionsOf(greenGradient, cornerRadius8, AlignItems.Center, JustifyContent.Center)

@Adaptive
fun someFun() {

    row(2.gridCol, someStyles) {
        text("Hello World!", white)
    }

}
```

You can mix groups with single ones:

```kotlin
val centered = instructionsOf(AlignItems.Center, JustifyContent.Center)

@Adaptive
fun someFun() {

    row(2.gridCol, centered, greenGradient, cornerRadius8) {
        text("Hello World!", white)
    }

}
```

## Non-UI instructions

Instructions are not limited to styling nor to UI fragments. This is a backend
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

## Executable instructions

If you implement `AdaptiveInstruction.execute` in your instruction you can call it very easily.

Check the `onClick` in the following example. It calls the `execute` method of `Replace` if
there is a `Replace` instruction.

This invoke is no-op when there is no such instruction.

```kotlin
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.invoke

@Adaptive
fun link(label: String, vararg instructions: AdaptiveInstruction) {
    text(label, onClick { instructions<Replace>() })
}
```

## Defining your own

> [!NOTE]
>
> It is good practice to make all instruction classes immutable and Adat classes. 
> This is not currently enforced, but it might be in the future.
>

Simply create a class or an object that implements `AdaptiveInstruction`:

```kotlin
@Adat
class SomeInstruction(val someString: String) : AdaptiveInstruction

@Adat
object SomeOtherInstruction : AdaptiveInstruction
```

## Finding fragments with a given instruction

Use one of:

- `firstWith`
- `firstOrNullWith`
- `singleWith`
- `filterWith`

```kotlin
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

To let a fragment accept instructions, add a `vararg` parameter called `instructions` with
the type `AdaptiveInstruction`.

To access instructions of another fragment from your manual implementation
use `AdaptiveFragment.instruction`.

The value of this property is the current instruction set from the state 
of the component, type is `Array<out AdaptiveInstruction>`.

If a given fragment does not have instructions (no parameter with the name
and type) you get an empty array.

The index of the instruction state variable is stored in the `instructionIndex`
property of the fragment.

## Instructions constraints

> [!NOTE]
> 
> This is a planned feature, it is not implemented yet.
> 

Instructions may have constraints to help avoid errors. For example `onDrop` has no
use outside of `dropTarget` fragment.

In this case the instruction class/function should be annotated with `@InstructionConstraint`:

```kotlin
@InstructionConstraint(fragment = "aui:dropTarget")
fun <T> onDrop(data : T?, handler : (data : T) -> Unit) = OnDrop<T>(data, handler) 
```