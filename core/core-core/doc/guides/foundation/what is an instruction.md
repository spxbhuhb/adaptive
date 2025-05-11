# What is an instruction

All [fragments](def://) carry a list of **instructions**, which can influence styling, layout, or behavior.
These are commonly used in UI components (e.g., `text`, `row`) but are not limited to them.

By definition, the first state variable of **ALL** fragments is the list of instructions passed
to that fragment.

> [!IMPORTANT]
> Adding instructions to a fragment does **not** affect rendering unless passed to an actual UI fragment
> like `text` or `row`.

Example:

```kotlin
@Adaptive
fun someFun() {
    row(borders.outline, cornerRadius8) {
        alignItems.center .. alignSelf.start
        padding { 16.dp }
        text("Hello World!") .. textSmall .. onClick { println("Hello") }
    }
}
```

## Instruction Placement Styles

There are three ways to pass instructions to a fragment:

- **Argument instructions** – as parameters to the function (if the function has such a parameter)
- **Inner instructions** – as statements immediately inside the fragment lambda.
- **Outer instructions** – using the `..` (rangeTo) operator after the fragment.

In the example above:

* arguments of `row` are argument instructions
* first two lines in the lambda of `row` are inner instructions
* the ones after `text` are outer instructions

The `..` (rangeTo) operator can be used to chain instructions together.

These styles are functionally equivalent (but have different precedence).

### Precedence Order

When multiple instruction methods are used:

1. Outer (strongest)
2. Inner
3. Argument (weakest)

Within each group, later declarations override earlier ones.

### Argument Instructions

Fragments that accept argument instructions declare a `vararg instructions: AdaptiveInstruction`
parameter.

The `instructions` parameter **cannot be accessed directly** in the function, the plugin
reports an error if you try that.

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

### Inner Instructions

These improve code readability by putting instructions at the top of fragment blocks.

```kotlin
@Adaptive
fun someFun(fixed : Boolean) {
    grid {
        rowTemplate(260.dp, 1.fr)
        if (fixed) colTemplate(200.dp) else colTemplate(1.fr)
        text("Hello World!") .. alignSelf.center
    }
}
```

* In this example, `grid` has two inner instructions.
* You can use `if`, `when` or any other expression that actually returns with an instruction.
* Instructions **MUST** follow the opening bracket, **you cannot mix rendering statements and inner instructions**.

### Outer Instructions

* Use the `..` (rangeTo) operator to add outer instructions.
* Works only if the fragment supports it (returns with `AdaptiveFragment`).
* Result in much more readable code.
* You can use `instructions()` deeper in the rendering,
  * but it **always** returns with the instructions of the **declaring fragment** (`navButton` in this example).

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

## Reactivity

Instructions are part of the fragment state, they are **fully reactive**.

```kotlin
@Adaptive
fun someFun(fixed : Boolean) {
    grid {
        if (fixed) colTemplate(200.dp) else colTemplate(1.fr)
        text("Hello World!") .. alignSelf.center
    }
}
```

1. In the example above, when
2. `fixed` changes, then
3. `grid` `colTemplate` changes, then
4. the whole grid layout is updated, then
5. `text` will be repositioned according to the available space.

## Reusing and Grouping Instructions

Reuse instructions via named variables or instruction groups:

```kotlin
val greenGradient = backgroundGradient(90, lightGreen, mediumGreen)
val cornerRadius8 = cornerRadius(8)

val someStyles = instructionsOf(greenGradient, cornerRadius8, AlignItems.Center, JustifyContent.Center)

row(2.gridCol, someStyles) {
    text("Hello World!", white)
}
```

Groups can be mixed with individual instructions.

## Non-UI instructions

Instructions are not limited to styling nor to UI fragments. This is a backend
instruction:

```kotlin

class DelayStart(val duration: Duration) : AdaptiveInstruction

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

## Defining Custom Instructions

> [!NOTE]
>
> It is good practice to make all instruction classes immutable and Adat classes.
> This is currently the best practice and may become enforced in future versions.
>

Simply create a class or an object that implements `AdaptiveInstruction`:

```kotlin
@Adat
class SomeInstruction(val someString: String) : AdaptiveInstruction

@Adat
object SomeOtherInstruction : AdaptiveInstruction
```

## Querying Instructions at Runtime

Search the fragment tree using:

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
and type), you get an empty array.

The index of the instruction state variable is stored in the `instructionIndex`
property of the fragment.

## Instructions constraints (planned feature)

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

# See Also

- [What is a fragment](guide://)