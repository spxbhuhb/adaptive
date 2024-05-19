# Instructions

A fragment may have so-called instructions. The best example is styling of UI fragments, but
instructions are not limited to styles.

To let a fragment accept instructions add a `vararg` parameter called `instructions` with 
the type `AdaptiveInstruction`.

This example checks is VIPOnly is in the instructions and acts differently based on the 
result.

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveInstruction

object VIPOnly : AdaptiveInstuction

@Adaptive

fun someFun(isVIP: Boolean, vararg instructions: AdaptiveInstruction) {

    if (VIPOnly in instructions && ! isVIP) return

    text("Only important people can see this!")

}
```

A more conservative example is to add styling to a UI element.

These are built-in instructions from the `ui-common` module:

- `blue` - set the foreground color to blue
- `padding_10` - add 10 pixels of padding to the fragment
- `bodySmall` - use the body small text settings

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.instruction.color.blue

@Adaptive

fun blueTextWithPadding(content : String) {
    text(content, blue, padding_12, bodySmall)
}
```

## Manual Implementation

To access instructions of another fragment from your manual implementation
use `AdaptiveFragment.instruction`.

The value of this property is the current instruction set from the state 
of the component, type is `Array<out AdaptiveInstruction>`.

If a given fragment does not have instructions (no parameter with the name
and type) you get an empty array.

The index of the instruction state variable is stored in the `instructionIndex`
property of the fragment.
